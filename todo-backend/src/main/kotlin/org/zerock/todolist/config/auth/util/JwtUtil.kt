package org.zerock.todolist.config.auth.util

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.zerock.todolist.config.auth.RedisService
import java.nio.charset.StandardCharsets
import java.time.ZonedDateTime
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtUtil(
    private val redisService: RedisService
) {
    @Value("\${JWT_KEY}")
    private lateinit var key: String

    fun generateToken(valueMap: Map<String, Any>, min: Long): String { // JWT 문자열 생성
        val key: SecretKey?

        try {
            key = Keys.hmacShaKeyFor(this.key.toByteArray(StandardCharsets.UTF_8))
        } catch (e: Exception) {
            throw RuntimeException(e.message)
        }

        return Jwts.builder()
            .setHeader(mapOf<String, Any>("typ" to "JWT"))
            .setClaims(valueMap)
            .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
            .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant()))
            .signWith(key)
            .compact()
    }

    fun validateToken(token: String): Map<String, Any> {
        val claim: Map<String, Any>?

        try {
            val key = Keys.hmacShaKeyFor(this.key.toByteArray(StandardCharsets.UTF_8))

            claim = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token) // 파싱 및 검증, 실패 시 Error
                .body
        } catch (e: MalformedJwtException) {
            throw CustomJwtException("MalFormed")
        } catch (e: ExpiredJwtException) {
            throw CustomJwtException("Expired")
        } catch (e: InvalidClaimException) {
            throw CustomJwtException("Invalid")
        } catch (e: JwtException) {
            throw CustomJwtException("JWTError")
        } catch (e: Exception) {
            throw CustomJwtException("Error")
        }

        return claim
    }

    fun generateTokenToCookie(claims: MutableMap<String, Any>, response: HttpServletResponse) {
        val accessToken = generateToken(claims, 60)
        val refreshToken = generateToken(claims, 60 * 24)

        redisService.create(claims["userId"].toString(), refreshToken, 24 * 30)

        val refreshTokenCookie = Cookie("TODOLIST_REFRESHTOKEN", refreshToken)
        refreshTokenCookie.path = "/" // 모든 경로에서, 하위 경로를 지정할 경우 해당 경로의 하위 경로에서만 접근 가능
        refreshTokenCookie.maxAge = 60 * 60 * 24 * 30 // 유효기간(초)
        refreshTokenCookie.secure = true // 보안 채널(HTTPS)을 통해 전송되는 경우 쿠키 전송(암호화 되지 않은 요청에 쿠키 전달 X)
        refreshTokenCookie.isHttpOnly = true // 브라우저에서 쿠키 접근 X(document.cookie X), HTTP 통신으로만 접근

        response.status = HttpStatus.OK.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE

        response.addCookie(refreshTokenCookie) // 응답 헤더에 Cookie를 포함

        // accessToken은 Client LocalStorage에 저장하기 위해 응답 본문으로 보냄
        jacksonObjectMapper().writeValue(
            response.writer,
            mapOf("userId" to claims["userId"], "accessToken" to accessToken)
        )
    }

    fun deleteTokenToCookie(userId: String, response: HttpServletResponse) {

        redisService.delete(userId)

        val refreshTokenCookie = Cookie("TODOLIST_REFRESHTOKEN", null)
        refreshTokenCookie.path = "/" // 모든 경로에서, 하위 경로를 지정할 경우 해당 경로의 하위 경로에서만 접근 가능
        refreshTokenCookie.maxAge = 0 // 유효기간(초)
        refreshTokenCookie.secure = true // 보안 채널(HTTPS)을 통해 전송되는 경우 쿠키 전송(암호화 되지 않은 요청에 쿠키 전달 X)
        refreshTokenCookie.isHttpOnly = true // 브라우저에서 쿠키 접근 X(document.cookie X), HTTP 통신으로만 접근

        response.status = HttpStatus.NO_CONTENT. value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE

        response.addCookie(refreshTokenCookie) // 응답 헤더에 Cookie를 포함
    }
}