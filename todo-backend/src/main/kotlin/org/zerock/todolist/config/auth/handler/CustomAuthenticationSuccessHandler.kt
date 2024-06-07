package org.zerock.todolist.config.auth.handler

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.zerock.todolist.config.auth.CustomUserDetails
import org.zerock.todolist.config.auth.util.JwtUtil

@Component
class CustomAuthenticationSuccessHandler(
    private val jwtUtil: JwtUtil
) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val user: CustomUserDetails = authentication.principal as CustomUserDetails
        val claims: MutableMap<String, Any> = user.getClaims().toMutableMap()

        val accessToken = jwtUtil.generateToken(claims, 60) // 60분
        val refreshToken = jwtUtil.generateToken(claims, 60 * 24) // 24시간

        claims["accessToken"] = accessToken
        claims["refreshToken"] = refreshToken

        // TODO: refreshToken을 HttpOnly로 저장
        val cookie = Cookie("TODOLIST_REFRESHTOKEN", refreshToken)
        cookie.path = "/" // 모든 경로에서
        cookie.maxAge = 60 * 60 * 24 * 30 // 유효기간(초)
        cookie.isHttpOnly = true // 브라우저에서 쿠키 접근 X

        response.status = HttpStatus.OK.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE

        jacksonObjectMapper().writeValue(response.writer, accessToken)
    }
}