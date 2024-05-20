package org.zerock.todolist.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.context.DelegatingSecurityContextRepository
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository
import org.zerock.todolist.config.auth.CustomAuthenticationFailureHandler
import org.zerock.todolist.config.auth.CustomAuthenticationSuccessHandler
import org.zerock.todolist.config.auth.JsonUsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val objectMapper: ObjectMapper,
    private val userDetailsService: UserDetailsService,
    private val customAuthenticationSuccessHandler: CustomAuthenticationSuccessHandler,
    private val customAuthenticationFailureHandler: CustomAuthenticationFailureHandler,
) {
    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun configure(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() }

        return http
            .addFilterBefore(
                jsonUsernamePasswordAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter::class.java
            )
            .authorizeHttpRequests {
                it.requestMatchers(HttpMethod.GET).permitAll()
                    .requestMatchers("/signup", "/signin").permitAll()
                    .anyRequest().authenticated()
            }
            .formLogin { it.disable() }
            .logout { it.invalidateHttpSession(true) }
            .build()
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