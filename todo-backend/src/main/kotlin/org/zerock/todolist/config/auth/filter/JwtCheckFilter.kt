package org.zerock.todolist.config.auth.filter

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import org.zerock.todolist.config.auth.util.JwtUtil

class JwtCheckFilter(
    private val jwtUtil: JwtUtil
) : OncePerRequestFilter() { // OncePerRequestFilter : 모든 요청에 대해 체크할 때 사용

    override fun shouldNotFilter(request: HttpServletRequest): Boolean { // 필터로 체크하지 않을 경로 or 메서드 지정
        val path = request.requestURI
        val urls = listOf("/users/signin", "/users/signup")

        // // GET 요청은 필터링 X || path의 접두사와 일치하는 URI가 있으면 필터 체크 X
        return "GET" == request.method || urls.any { path.startsWith(it) }
    }

    override fun doFilterInternal(
        // 모든 요청에 대해 체크하려고 할 때 사용
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val authHeader = request.getHeader("Authorization")

        try {
            val accessToken = authHeader.substring(7) // Bearer
            val claims = jwtUtil.validateToken(accessToken)
            val userId = claims["userId"].toString().toLong()
            val authenticationToken = UsernamePasswordAuthenticationToken(userId, null)

            SecurityContextHolder.getContext().authentication = authenticationToken

            filterChain.doFilter(request, response)
        } catch (e: Exception) {
            logger.error(e.message)

            var errorMessage = e.message

            if (e.message == null) { // 헤더에 토큰을 넣지 않은 경우: NullPointException
                errorMessage = "No token"
            }

            response.status = HttpStatus.BAD_REQUEST.value()
            response.contentType = MediaType.APPLICATION_JSON_VALUE

            jacksonObjectMapper().writeValue(response.writer, errorMessage)
        }
    }
}