package org.zerock.todolist.domain.todo.model

import jakarta.persistence.*
import org.zerock.todolist.domain.BaseEntity
import org.zerock.todolist.domain.comment.model.Comment
import org.zerock.todolist.domain.comment.model.toResponse
import org.zerock.todolist.domain.todo.dto.CreateTodoRequest
import org.zerock.todolist.domain.todo.dto.TodoListResponse
import org.zerock.todolist.domain.todo.dto.TodoResponse
import org.zerock.todolist.domain.user.model.User

@Entity
@Table(name = "todo")
class Todo private constructor(
    var title: String,
    var content: String,
    var writer: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

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

fun Todo.toListResponse(): TodoListResponse {
//    return TodoListResponse(id!!, title, content, user.id!! ,writer, createdAt, completed}) // 프런트용
    return TodoListResponse(
        id!!,
        title,
        content,
        writer,
        createdAt,
        completed,
        comments.map { it.toResponse() }) // STEP 4 과제용
}