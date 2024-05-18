package org.zerock.todolist.config.auth

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.zerock.todolist.domain.user.model.User

class CustomUserDetails(
    private val user: User
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        val collector: MutableCollection<GrantedAuthority> = ArrayList()
        collector.add(GrantedAuthority { user.role })

        return collector
    }

    override fun getPassword(): String {
        return user.password
    }

    override fun getUsername(): String {
        return user.email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}