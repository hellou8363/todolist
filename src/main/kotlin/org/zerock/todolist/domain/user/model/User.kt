package org.zerock.todolist.domain.user.model

import jakarta.persistence.*
import org.zerock.todolist.domain.user.dto.UserResponse
import java.time.LocalDateTime

@Entity
@Table(name = "todo_user")
class User(
    val email: String,
    val nickname: String,
    val password: String,
    val role: String,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    ) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}

fun User.toResponse(): UserResponse {
    return UserResponse(id!!, email, nickname, createdAt)
}