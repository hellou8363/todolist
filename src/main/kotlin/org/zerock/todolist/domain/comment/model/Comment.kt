package org.zerock.todolist.domain.comment.model

import jakarta.persistence.*
import org.zerock.todolist.domain.comment.dto.CommentResponse
import org.zerock.todolist.domain.todo.model.Todo
import org.zerock.todolist.domain.user.model.User
import java.time.LocalDateTime

@Entity
@Table(name = "comment")
class Comment(
    @Column(name = "content")
    var content: String,

    @Column(name = "writer")
    var writer: String,

    @Column(name = "password")
    var password: String,

    @Column(name = "created_at")
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id")
    val todo: Todo,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}

fun Comment.toResponse(): CommentResponse {
    return CommentResponse(id!!, content, writer, createdAt)
}