package org.zerock.todolist.config.auth

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.util.StreamUtils
import java.nio.charset.StandardCharsets


class JsonUsernamePasswordAuthenticationFilter(
    private val objectMapper: ObjectMapper
) : AbstractAuthenticationProcessingFilter(DEFAULT_LOGIN_PATH_REQUEST_MATCHER) {

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        if (request.contentType == null || request.contentType != CONTENT_TYPE) {
            throw AuthenticationServiceException("Authentication Content-Type not supported: " + request.contentType)
        }

        val loginDto: LoginDto = objectMapper.readValue(
            StreamUtils.copyToString(request.inputStream, StandardCharsets.UTF_8),
            LoginDto::class.java
        )

        val username: String = loginDto.email
        val password: String = loginDto.password

        val authRequest = UsernamePasswordAuthenticationToken(username, password)

        setDetails(request, authRequest)

        return authenticationManager.authenticate(authRequest)
    }

    private fun setDetails(request: HttpServletRequest?, authRequest: UsernamePasswordAuthenticationToken) {
        authRequest.details = authenticationDetailsSource.buildDetails(request)
    }

    private data class LoginDto(
        var email: String,
        var password: String
    ) {
        constructor() : this("", "")
    }

    companion object {
        private const val DEFAULT_LOGIN_REQUEST_URL = "/signin"
        private const val HTTP_METHOD = "POST"
        private const val CONTENT_TYPE = "application/json"
        private val DEFAULT_LOGIN_PATH_REQUEST_MATCHER = AntPathRequestMatcher(DEFAULT_LOGIN_REQUEST_URL, HTTP_METHOD)
    }
}