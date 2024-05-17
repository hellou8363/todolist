package org.zerock.todolist.domain.user.model

import jakarta.persistence.*
import org.zerock.todolist.domain.user.dto.UserResponse
import java.time.LocalDateTime

@Entity
@Table(name = "todo_user")
class User(
    @Column(name = "email")
    val email: String,

    @Column(name = "nickname")
    val nickname: String,

    @Column(name = "password")
    val password: String,

    @Column(name = "role")
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