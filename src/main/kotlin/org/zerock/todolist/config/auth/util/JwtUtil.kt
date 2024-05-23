package org.zerock.todolist.config.auth.util

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.time.ZonedDateTime
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtUtil {
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
}