package org.zerock.todolist.config.auth.filter

import com.fasterxml.jackson.module.kotlin.jsonMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.util.StreamUtils
import java.nio.charset.StandardCharsets


class JsonUsernamePasswordAuthenticationFilter : AbstractAuthenticationProcessingFilter(DEFAULT_LOGIN_PATH_REQUEST_MATCHER) {
    // AbstractAuthenticationProcessingFilter 내 AuthenticationSuccessHandler와 AuthenticationFailureHandler가 있음

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {

        // contentType이 null이거나 application/json이 아니면 예외 발생
        if (request.contentType == null || request.contentType != CONTENT_TYPE) {
            throw AuthenticationServiceException("Authentication Content-Type not supported: " + request.contentType)
        }

        // request의 응답 본문을 읽어서 DTO로 변환
        val loginDto: LoginDto = jsonMapper().readValue(
            StreamUtils.copyToString(request.inputStream, StandardCharsets.UTF_8),
            LoginDto::class.java
        )

        // UsernamePasswordAuthenticationToken : 사용자의 인증 정보(사용자 ID = Principal, 비밀번호 = Credential)를 담는 역할
        // authRequest는 다음과 같은 값을 갖음
        // authRequest: UsernamePasswordAuthenticationToken [Principal=1234@naver.com, Credentials=[PROTECTED], Authenticated=false, Details=null, Granted Authorities=[]]
        val authRequest = UsernamePasswordAuthenticationToken(loginDto.email, loginDto.password)
        setDetails(request, authRequest)

        return authenticationManager.authenticate(authRequest) // 검증: 요청으로 받은 사용자 정보와 DB에 저장된 사용자 정보를 비교 인증 수행
        // 1. AbstractAuthenticationProcessingFilter.getAuthenticationManager -> this.authenticationManager
        // 2. ProviderManager.authenticate -> result: Authentication
    }

    // 사용자 요청(request)에서 추출한 세부 정보를 사용자의 인증 정보에 세부 정보를 추가
    private fun setDetails(request: HttpServletRequest?, authRequest: UsernamePasswordAuthenticationToken) {
        authRequest.details = authenticationDetailsSource.buildDetails(request)
        // authRequest.details는 다음과 같은 정보를 반환
        // WebAuthenticationDetails [RemoteIpAddress=0:0:0:0:0:0:0:1, SessionId=null]
        // Hibernate: select u1_0.id,u1_0.created_at,u1_0.email,u1_0.nickname,u1_0.password,u1_0.role from todo_user u1_0 where u1_0.email=?

        // 1. authenticationDetailsSource = new WebAuthenticationDetails(context)
        // 2. WebAuthenticationDetails -> this(request.getRemoteAddr(), extractSessionId(request))
        // 3. WebAuthenticationDetails -> this.remoteAddress = remoteAddress, this.sessionId = sessionId
        // 4. AbstractAuthenticationToken.setDetails -> this.details = details -> createSuccessAuthentication(principalToReturn, authentication, user)
        // 5. doFilter -> ...
    }

    private data class LoginDto(
        var email: String,
        var password: String
    ) {
        // 기본 생성자가 없으면, InvalidDefinitionException: Cannot construct instance
        constructor() : this("", "")
    }

    companion object {
        private const val DEFAULT_LOGIN_REQUEST_URL = "/users/signin"
        private const val HTTP_METHOD = "POST"
        private const val CONTENT_TYPE = "application/json"
        private val DEFAULT_LOGIN_PATH_REQUEST_MATCHER =
            AntPathRequestMatcher(DEFAULT_LOGIN_REQUEST_URL, HTTP_METHOD) // POST + "/users/signin"
    }
}