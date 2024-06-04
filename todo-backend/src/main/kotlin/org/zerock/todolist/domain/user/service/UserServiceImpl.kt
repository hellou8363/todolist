package org.zerock.todolist.domain.user.service

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.zerock.todolist.config.auth.CustomUserDetails
import org.zerock.todolist.domain.exception.AlreadyExistsException
import org.zerock.todolist.domain.user.dto.CreateUserRequest
import org.zerock.todolist.domain.user.dto.SigninRequest
import org.zerock.todolist.domain.user.dto.UserResponse
import org.zerock.todolist.domain.user.model.User
import org.zerock.todolist.domain.user.model.toResponse
import org.zerock.todolist.domain.user.repository.UserRepository

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {

    @Transactional
    override fun createUser(request: CreateUserRequest): UserResponse {
        val isExistEmail = userRepository.existsByEmail(request.email)

        if (isExistEmail) {
            throw AlreadyExistsException(request.email)
        }

        return userRepository.save(
            User.from(request)
        ).toResponse()
    }

    override fun signinUser(request: SigninRequest): UserResponse {
        val user = userRepository.findByEmail(request.email)

        return user.toResponse()
    }

    override fun getUserDetails(): CustomUserDetails? {
        val principal = SecurityContextHolder.getContext().authentication.principal
        return if (principal is CustomUserDetails) principal else null
    }
}


