package org.zerock.todolist.config.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.zerock.todolist.domain.user.model.toResponse
import org.zerock.todolist.domain.user.repository.UserRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class CustomAuthenticationSuccessHandler(
    private val userRepository: UserRepository,
) : AuthenticationSuccessHandler {
    private val objectMapper = ObjectMapper()

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val user: CustomUserDetails = authentication.principal as CustomUserDetails
        val userToResponse = userRepository.findByEmail(user.username).toResponse()

        response.status = HttpStatus.OK.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE

        val DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSSSSS"

        objectMapper.registerModule(
            JavaTimeModule().addSerializer(
                LocalDateTime::class.java, LocalDateTimeSerializer(
                    DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)
                )
            )
        ).writeValue(response.writer, userToResponse)
    }
}