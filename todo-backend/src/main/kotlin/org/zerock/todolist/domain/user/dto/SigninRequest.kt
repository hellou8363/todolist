package org.zerock.todolist.domain.user.dto

data class SigninRequest(
    val email: String,
    val password: String,
)
