package org.zerock.todolist.domain.user.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.client.WebClient
import org.zerock.todolist.config.auth.CustomUserDetails
import org.zerock.todolist.config.auth.util.CustomJwtException
import org.zerock.todolist.config.auth.util.JwtUtil
import org.zerock.todolist.domain.exception.AlreadyExistsException
import org.zerock.todolist.domain.user.dto.CreateUserRequest
import org.zerock.todolist.domain.user.dto.SigninRequest
import org.zerock.todolist.domain.user.dto.UserResponse
import org.zerock.todolist.domain.user.model.User
import org.zerock.todolist.domain.user.model.toResponse
import org.zerock.todolist.domain.user.repository.UserRepository
import java.util.*

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val jwtUtil: JwtUtil
) : UserService {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Transactional
    override fun createUser(request: CreateUserRequest, joinType: String): UserResponse { // 일반 회원가입
        val isExistEmail = userRepository.existsByEmail(request.email)

        if (isExistEmail) {
            throw AlreadyExistsException(request.email)
        }

        return userRepository.save(
            User.from(request, joinType)
        ).toResponse()
    }

    override fun signinUser(request: SigninRequest, response: HttpServletResponse) { // 로그인 처리(token 발급)
        val user = userRepository.findByEmail(request.email)

        val userDetails = CustomUserDetails(user)

        val clams: MutableMap<String, Any> = userDetails.getClaims().toMutableMap()

        val accessToken = jwtUtil.generateToken(clams, 60)
        val refreshToken = jwtUtil.generateToken(clams, 60 * 24)

        val refreshTokenCookie = Cookie("TODOLIST_REFRESHTOKEN", refreshToken)
        refreshTokenCookie.path = "/" // 모든 경로에서, 하위 경로를 지정할 경우 해당 경로의 하위 경로에서만 접근 가능
        refreshTokenCookie.maxAge = 60 * 60 * 24 * 30 // 유효기간(초)
        refreshTokenCookie.secure = true // 보안 채널(HTTPS)을 통해 전송되는 경우 쿠키 전송(암호화 되지 않은 요청에 쿠키 전달 X)
        refreshTokenCookie.isHttpOnly = true // 브라우저에서 쿠키 접근 X(document.cookie X), HTTP 통신으로만 접근

        response.status = HttpStatus.OK.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE

        response.addCookie(refreshTokenCookie) // 응답 헤더에 Cookie를 포함

        // accessToken은 Client LocalStorage에 저장하기 위해 응답 본문으로 보냄
        jacksonObjectMapper().writeValue(response.writer, mapOf("accessToken" to accessToken))
    }

    override fun getUserDetails(): CustomUserDetails? {
        val principal = SecurityContextHolder.getContext().authentication.principal
        return if (principal is CustomUserDetails) principal else null
    }

    fun refresh(authHeader: String, refreshToken: String): Map<String, Any> {
        if (authHeader.length < 7) {
            throw CustomJwtException("INVALID_STRING")
        }

        val accessToken = authHeader.substring(7)

        if (!checkExpiredToken(accessToken)) { // AccessToken이 만료되지 않았다면 그대로 반환
            return mapOf("accessToken" to accessToken, "refreshToken" to refreshToken)
        }

        val claims = jwtUtil.validateToken(refreshToken)
        val newAccessToken = jwtUtil.generateToken(claims, 60) // 60분
        val newRefreshToken =
            if (checkTime(claims["exp"] as Int)) jwtUtil.generateToken(claims, 60 * 24) else refreshToken

        return mapOf("accessToken" to newAccessToken, "refreshToken" to newRefreshToken)
    }

    fun checkTime(exp: Int): Boolean { // 1시간 미만 여부
        val expDate = Date(exp.toLong() * 1000) // JWT exp를 날짜로 변환
        val gap = expDate.time - System.currentTimeMillis() // 현재 시간과 차이 계산(ms)
        val leftMin = gap / (1000 * 60)
        println("checkTime - expDate: $expDate, gap: $gap, leftMin: $leftMin")

        return leftMin < exp
    }

    fun checkExpiredToken(token: String): Boolean {
        try {
            jwtUtil.validateToken(token)
        } catch (e: CustomJwtException) {
            if (e.message == "Expired") {
                return true
            }
        }
        return false
    }

    @Transactional
    override fun signWithKakao(accessToken: String, response: HttpServletResponse) {
        val kakaoUserInfo = getUserFromKakao(accessToken)

        try {
            val userInfo = userRepository.findByEmail(kakaoUserInfo["email"] as String)

            // 이미 카카오 소셜로 가입된 회원이면 token 발급
            if (userInfo.joinType.contains("KAKAO")) {
                return signinUser(
                    SigninRequest(
                        kakaoUserInfo["email"] as String,
                        "null"
                    ),
                    response
                )
            }

            // 일반회원으로 가입된 적이 있는 경우 가입종류에 KAKAO 추가 후 token 발급
            userInfo.joinType = "${userInfo.joinType}, KAKAO"

            return signinUser(
                SigninRequest(
                    kakaoUserInfo["email"] as String,
                    "null"
                ),
                response
            )
        } catch (e: EmptyResultDataAccessException) { // 신규회원이면 회원가입 처리 후 로그인
            createUser(
                CreateUserRequest(
                    kakaoUserInfo["email"].toString(),
                    kakaoUserInfo["nickname"].toString(),
                    "null"
                ),
                joinType = "KAKAO"
            )

            return signinUser(
                SigninRequest(
                    kakaoUserInfo["email"] as String,
                    "null"
                ),
                response
            )
        }

    }

    override fun getUserFromKakao(accessToken: String): Map<String, String> { // 엑세스 토큰으로 사용자 정보 가져오기
        // 토큰 값이 null인 경우, 카카오 서버측에서는 우리가 클라이언트이기 때문에 400을 우리에게 던짐
        // 그러면 우리의 클라이언트 쪽에는 500에러가 전달되므로 여기서 예외를 잡아서 프런트 쪽으로 전달해야 함
        if (accessToken == "undefined") {
            throw IllegalArgumentException("Access token cannot be empty")
        }

        val responseBody = WebClient.create("https://kapi.kakao.com/v2/user/me")
            .get()
            .header("Authorization", "Bearer $accessToken")
            .header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
            .retrieve() // 응답값을 받게 해주는 메서드
            .bodyToMono(Map::class.java) // ResponseBody Type
            .block() // 동기 방식으로 변경

        val kakaoAccount = responseBody?.get("kakao_account") as Map<*, *>
        val email = kakaoAccount["email"].toString()
        val profile = kakaoAccount["profile"] as Map<*, *>
        val nickname = profile["nickname"].toString()

        return mapOf("email" to email, "nickname" to nickname)
    }

    override fun getUserFromNaver(accessToken: String): UserResponse {
        TODO("Not yet implemented")
    }


}


