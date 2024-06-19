package org.zerock.todolist.domain.user.service

import jakarta.servlet.http.HttpServletResponse
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.client.WebClient
import org.zerock.todolist.config.auth.CustomUserDetails
import org.zerock.todolist.config.auth.RedisService
import org.zerock.todolist.config.auth.util.CustomJwtException
import org.zerock.todolist.config.auth.util.JwtUtil
import org.zerock.todolist.domain.user.dto.CreateUserRequest
import org.zerock.todolist.domain.user.dto.SigninRequest
import org.zerock.todolist.domain.user.dto.UserResponse
import org.zerock.todolist.domain.user.model.User
import org.zerock.todolist.domain.user.model.toResponse
import org.zerock.todolist.domain.user.repository.UserRepository
import org.zerock.todolist.exception.AlreadyExistsException
import org.zerock.todolist.exception.ModelNotFoundException
import java.util.*

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val jwtUtil: JwtUtil,
    private val redisService: RedisService
) : UserService {

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

    override fun getUser(userId: Long): User {
        return userRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("User", userId)
    }

    override fun signinUser(request: SigninRequest, response: HttpServletResponse) { // 로그인 처리(token 발급)
        val user = userRepository.findByEmail(request.email)
        val userDetails = CustomUserDetails(user)
        val claims: MutableMap<String, Any> = userDetails.getClaims().toMutableMap()

        jwtUtil.generateTokenToCookie(claims, response)
    }

    override fun refresh(accessToken: String, refreshToken: String, response: HttpServletResponse) {
        // accessToken, refreshToken 검증
        jwtUtil.validateToken(accessToken.split(" ")[1])

        val refreshTokenUserId = jwtUtil.validateToken(refreshToken)["userId"]
        // userId에 일치하는 DB의 refreshToken 가져오기
        val dbToken = redisService.getRefreshToken(refreshTokenUserId.toString())

        if (refreshToken == dbToken) {
            val userInfo =
                userRepository.findByIdOrNull(refreshTokenUserId.toString().toLong()) ?: ModelNotFoundException(
                    "User",
                    null
                )
            val claims = CustomUserDetails(userInfo as User).getClaims().toMutableMap()

            // TODO: refresh token은 그대로 유지되도록 별도 메서드 필요
            jwtUtil.generateTokenToCookie(claims, response)
        }
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
    override fun signinWithKakao(accessToken: String, response: HttpServletResponse) {
        val kakaoUserInfo = getUserFromKakao(accessToken)

        // id, 개인정보, joinType = EMAIL, KAKAO, ..
        try {
            val userInfo = userRepository.findByEmail(kakaoUserInfo["email"] as String)

            // 이미 카카오 소셜로 가입된 회원이면 로그인 처리
            if (userInfo.joinType.contains("KAKAO")) {
                return signinUser(
                    SigninRequest(
                        kakaoUserInfo["email"] as String,
                        "null"
                    ),
                    response
                )
            }

            // 일반회원으로 가입된 적이 있는 경우 가입종류에 KAKAO 추가 후 로그인 처리
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

    override fun logoutUser(userId: String, response: HttpServletResponse) {
        jwtUtil.deleteTokenToCookie(userId, response)
    }
}


