package org.zerock.todolist.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.context.DelegatingSecurityContextRepository
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.zerock.todolist.config.auth.CustomAuthenticationFailureHandler
import org.zerock.todolist.config.auth.CustomAuthenticationSuccessHandler
import org.zerock.todolist.config.auth.JsonUsernamePasswordAuthenticationFilter
import org.zerock.todolist.config.auth.JwtCheckFilter
import org.zerock.todolist.domain.exception.CustomAccessDeniedException
import org.zerock.todolist.util.JwtUtil


@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val objectMapper: ObjectMapper,
    private val userDetailsService: UserDetailsService,
    private val customAuthenticationSuccessHandler: CustomAuthenticationSuccessHandler,
    private val customAuthenticationFailureHandler: CustomAuthenticationFailureHandler
) {
    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun configure(http: HttpSecurity, jwtUtil: JwtUtil): SecurityFilterChain {
        return http
            .csrf { it.disable() } // API 서버에서 사용 X
            .cors { it.configurationSource(corsConfigurationSource()) } // cors 설정
            .addFilterBefore( // 우선 실행되어야 함
                JwtCheckFilter(jwtUtil), UsernamePasswordAuthenticationFilter::class.java
            )
            .addFilterBefore( // JSON 데이터 처리
                jsonUsernamePasswordAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter::class.java
            )
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) } // 서버 내부에서 세션 생성 X
            .formLogin { it.disable() } // 기본 로그인 폼 비활성화(기본 경로 "/login")
            .exceptionHandling { it.accessDeniedHandler(CustomAccessDeniedException("ERROR_ACCESSDENIED")) }
            .build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()

        configuration.setAllowedOriginPatterns(listOf("*")) // 모든 출처에서 요청 허용
        configuration.allowedMethods = listOf("HEAD", "GET", "POST", "PUT", "DELETE") // 허용 HTTP 메서드 지정
        configuration.allowedHeaders = listOf("Authorization", "Cache-Control", "Content-Type") // 허용 HTTP 헤더 지정
        configuration.allowCredentials = true // 자격 증명(쿠키, HTTP 인증, Client SSL 인증 등)

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration) // 모든 URL 패턴에 대해 설정한 configuration 적용

        return source
    }

    @Bean
    fun authenticationManager(): AuthenticationManager {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(userDetailsService)
        authProvider.setPasswordEncoder(bCryptPasswordEncoder())
        return ProviderManager(authProvider)
    }

    @Bean
    fun jsonUsernamePasswordAuthenticationFilter(): JsonUsernamePasswordAuthenticationFilter {
        val jsonUsernamePasswordAuthenticationFilter = JsonUsernamePasswordAuthenticationFilter(objectMapper)
        jsonUsernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManager())
        jsonUsernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler)
        jsonUsernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(customAuthenticationFailureHandler)
        jsonUsernamePasswordAuthenticationFilter.setSecurityContextRepository(
            DelegatingSecurityContextRepository(
                RequestAttributeSecurityContextRepository(
                    HttpSessionSecurityContextRepository().toString()
                )
            )
        )
        return jsonUsernamePasswordAuthenticationFilter
    }
}