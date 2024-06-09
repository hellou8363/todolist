package org.zerock.todolist.domain.user.service

import jakarta.servlet.http.HttpServletResponse
import org.zerock.todolist.config.auth.CustomUserDetails
import org.zerock.todolist.domain.user.dto.CreateUserRequest
import org.zerock.todolist.domain.user.dto.SigninRequest
import org.zerock.todolist.domain.user.dto.UserResponse

interface UserService {

    fun createUser(request: CreateUserRequest, joinType: String = "EMAIL"): UserResponse

    fun signinUser(request: SigninRequest, response: HttpServletResponse)

    fun signWithKakao(accessToken: String, response: HttpServletResponse)

    fun getUserDetails(): CustomUserDetails?

    fun getUserFromKakao(accessToken: String): Map<String, String>

    fun getUserFromNaver(accessToken: String): UserResponse

    fun logoutUser(userId: String, response: HttpServletResponse)

    fun refresh(accessToken: String, refreshToken: String, response: HttpServletResponse)
}