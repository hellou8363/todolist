package org.zerock.todolist.domain.comment.model

import jakarta.persistence.*
import org.zerock.todolist.domain.BaseEntity
import org.zerock.todolist.domain.comment.dto.CommentResponse
import org.zerock.todolist.domain.comment.dto.CreateAndUpdateCommentRequest
import org.zerock.todolist.domain.todo.model.Todo
import org.zerock.todolist.domain.user.model.User

@Entity
@Table(name = "comment")
class Comment private constructor(
    content: String,
    writer: String,
    val password: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id")
    val todo: Todo,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    var content: String = content
        protected set
    var writer: String = writer
        protected set

    companion object {
        fun from(createAndUpdateCommentRequest: CreateAndUpdateCommentRequest, todo: Todo, user: User): Comment {
            return Comment(
                createAndUpdateCommentRequest.content,
                createAndUpdateCommentRequest.writer,
                createAndUpdateCommentRequest.password,
                todo,
                user
            )
        }
    }

    fun update(content: String?, writer: String?) {
        content?.let { this.content = it }
        writer?.let { this.writer = it }
    }
}

fun Comment.toResponse(): CommentResponse {
    return CommentResponse(id!!, content, writer, createdAt)
}