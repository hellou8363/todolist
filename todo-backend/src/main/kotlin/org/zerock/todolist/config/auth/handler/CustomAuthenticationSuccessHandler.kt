package org.zerock.todolist.config.auth.handler

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
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

        jwtUtil.generateTokenToCookie(claims, response)
    }
}