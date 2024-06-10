package org.zerock.todolist.config.auth

import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.zerock.todolist.domain.user.model.User
import org.zerock.todolist.domain.user.repository.UserRepository


@Service
class UserDetailServiceImpl(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        var user: User

        try {
            user = userRepository.findByEmail(email)

            if(!user.joinType.contains("EMAIL")) {
                throw UsernameNotFoundException(email)
            }

        } catch (e: InternalAuthenticationServiceException) {
            throw UsernameNotFoundException(email)
        }

        return CustomUserDetails(user)
    }
}