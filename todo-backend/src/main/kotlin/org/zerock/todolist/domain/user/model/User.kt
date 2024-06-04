package org.zerock.todolist.domain.user.model

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.zerock.todolist.domain.user.dto.CreateUserRequest
import org.zerock.todolist.domain.user.dto.UserResponse
import java.time.LocalDateTime

@Entity
@Table(name = "todo_user")
class User private constructor(
    val email: String,
    val nickname: String,
    val password: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @CreationTimestamp
    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()

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