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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.zerock.todolist.config.auth.JsonUserNamePasswordAuthenticationFiler


@Configuration
@EnableWebSecurity
class SecurityConfig {
    private lateinit var objectMapper: ObjectMapper
    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun configure(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() } // CSRF 비활성화

        return http
            .authorizeHttpRequests { it ->
                it.requestMatchers(HttpMethod.GET).permitAll()
                    .requestMatchers("/signup", "/signin").permitAll()
                    .anyRequest().authenticated()
            }
            .formLogin { it.disable() } // TODO : form-data가 아닌 JSON으로 받을 수 있도록 수정 필요
//            .addFilterAfter(jsonUserNamePasswordAuthenticationFiler(), JsonUserNamePasswordAuthenticationFiler::class.java)
            .formLogin { it.usernameParameter("email").loginProcessingUrl("/signin") }
            .logout { it.invalidateHttpSession(true) }
            .build()
    }

    @Bean
    fun authenticationManager(): AuthenticationManager {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setPasswordEncoder(bCryptPasswordEncoder())
        return ProviderManager(authProvider)
    }

//    @Bean
//    fun jsonUserNamePasswordAuthenticationFiler(): JsonUserNamePasswordAuthenticationFiler {
//        val jsonUserNamePasswordAuthenticationFiler = JsonUserNamePasswordAuthenticationFiler(objectMapper)
//        jsonUserNamePasswordAuthenticationFiler.setAuthenticationManager(authenticationManager());
//        return jsonUserNamePasswordAuthenticationFiler;
//    }
}