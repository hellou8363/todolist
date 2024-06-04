package org.zerock.todolist.domain.todo.model

import jakarta.persistence.*
import org.zerock.todolist.domain.comment.model.Comment
import org.zerock.todolist.domain.comment.model.toResponse
import org.zerock.todolist.domain.todo.dto.CreateTodoRequest
import org.zerock.todolist.domain.todo.dto.TodoResponse
import org.zerock.todolist.domain.user.model.User
import java.time.LocalDateTime

@Entity
@Table(name = "todo")
class Todo private constructor(
    var title: String,
    var content: String,
    var writer: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()

    @Enumerated(EnumType.STRING)
    var completed: TodoCompleted = TodoCompleted.FALSE

    @OneToMany(mappedBy = "todo", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val comments: MutableList<Comment> = mutableListOf()

    companion object {
        fun from(createTodoRequest: CreateTodoRequest, user: User): Todo {
            return Todo(
                createTodoRequest.title,
                createTodoRequest.content,
                createTodoRequest.writer,
                user
            )
        }
    }
}

fun Todo.toResponse(): TodoResponse {
    return TodoResponse(id!!, title, content, writer, createdAt, completed, comments.map { it.toResponse() })
}
