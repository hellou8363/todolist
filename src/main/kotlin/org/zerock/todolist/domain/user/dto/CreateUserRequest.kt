package org.zerock.todolist.domain.user.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateUserRequest(
    @field:Email(message = "The email is not valid.")
    @field:NotBlank(message = "The email is not valid.")
    val email: String,
    @field:NotBlank(message = "The name cannot be blank.")
    @field:Size(min = 4, max = 10, message = "Nickname must be between 4 and 10")
    val nickname: String,
    @field:Size(min = 4, max = 10, message = "Password must be between 4 and 10")
    val password: String,
)
