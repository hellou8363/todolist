package org.zerock.todolist.config.auth

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.zerock.todolist.domain.user.model.User

// https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/core/userdetails/UserDetails.html
class CustomUserDetails(
    private val user: User
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> { // 사용자에게 부여된 권한을 반환
        val collector: MutableCollection<GrantedAuthority> = ArrayList()
        collector.add(GrantedAuthority { user.role })

        return collector
    }

    override fun getPassword(): String { // 사용자 비밀번호 반환
        return user.password
    }

    override fun getUsername(): String { // 사용자 이름 반환
        return user.email
    }

    override fun isAccountNonExpired(): Boolean { // 계정 만료 여부(만료이면 true, 아니면 false)
        return true
    }

    override fun isAccountNonLocked(): Boolean { // 계정 잠금 여부(잠기면 true, 아니면 false)
        return true
    }

    override fun isCredentialsNonExpired(): Boolean { // 자격 증명(비밀번호) 만료 여부(유효하면 true, 아니면 false)
        return true
    }

    override fun isEnabled(): Boolean { // 사용자 활성화 여부(활성화면 true, 아니면 false)
        return true
    }

    fun getClaims(): Map<String, Any> {
        val dataMap = mutableMapOf<String, Any>(
            "email" to user.email,
            "role" to user.role,
        )

        return dataMap
    }
}