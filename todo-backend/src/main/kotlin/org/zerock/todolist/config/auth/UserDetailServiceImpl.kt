package org.zerock.todolist.config.auth

import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.zerock.todolist.domain.user.repository.UserRepository


@Service
class UserDetailServiceImpl(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(userId: String): UserDetails {
        val user = userRepository.findByIdOrNull(userId.toLong()) ?: throw UsernameNotFoundException(userId)

        if (!user.joinType.contains("EMAIL")) {
            throw UsernameNotFoundException(userId)
        }

        return CustomUserDetails(user)
    }
}