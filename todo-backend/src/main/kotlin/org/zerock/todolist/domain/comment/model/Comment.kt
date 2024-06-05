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
    var content: String,
    var writer: String,
    var password: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id")
    val todo: Todo,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

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
}

fun Comment.toResponse(): CommentResponse {
    return CommentResponse(id!!, content, writer, createdAt)
}