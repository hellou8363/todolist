package org.zerock.todolist.config.auth

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.CredentialsExpiredException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationFailureHandler : AuthenticationFailureHandler {
    private val objectMapper = ObjectMapper()

    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException
    ) {
        var errMsg = "Invalid Username or Password"

        response.status = HttpStatus.UNAUTHORIZED.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE

        when (exception) {
            is BadCredentialsException -> {
                errMsg = "Invalid Username or Password"
            }

            is DisabledException -> {
                errMsg = "Locked"
            }

            is CredentialsExpiredException -> {
                errMsg = "Expired password"
            }
        }

        objectMapper.writeValue(response.writer, errMsg)
    }
}