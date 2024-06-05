package org.zerock.todolist.domain.user.model

import jakarta.persistence.*
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.zerock.todolist.domain.BaseEntity
import org.zerock.todolist.domain.user.dto.CreateUserRequest
import org.zerock.todolist.domain.user.dto.UserResponse

@Entity
@Table(name = "todo_user")
class User private constructor(
    val email: String,
    val nickname: String,
    val password: String
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    companion object {
        fun from(createUserRequest: CreateUserRequest): User {
            val encryptPassword = BCryptPasswordEncoder().encode(createUserRequest.password)

            return User(
                createUserRequest.email,
                createUserRequest.nickname,
                encryptPassword
            )
        }
    }
}

fun User.toResponse(): UserResponse {
    return UserResponse(id!!, email, nickname, createdAt)
}