package org.zerock.todolist.domain.user.service

import jakarta.servlet.http.HttpSession
import org.zerock.todolist.config.auth.CustomUserDetails
import org.zerock.todolist.domain.user.dto.CreateUserRequest
import org.zerock.todolist.domain.user.dto.SigninRequest
import org.zerock.todolist.domain.user.dto.UserResponse

interface UserService {

    fun createUser(request: CreateUserRequest): UserResponse

    fun signinUser(request: SigninRequest): UserResponse

    fun getUserDetails(): CustomUserDetails?
}