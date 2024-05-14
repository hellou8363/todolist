package org.zerock.todolist.domain.todo.model

import jakarta.persistence.*
import org.zerock.todolist.domain.comment.model.Comment
import org.zerock.todolist.domain.comment.model.toResponse
import org.zerock.todolist.domain.todo.dto.TodoListResponse
import org.zerock.todolist.domain.todo.dto.TodoResponse
import java.time.LocalDateTime

@Entity
@Table(name = "todo")
class Todo(
    @Column(name = "title")
    var title: String,

    @Column(name = "content")
    var content: String,

    @Column(name = "writer")
    var writer: String,

    @Column(name = "create_at")
    var createAt: LocalDateTime = LocalDateTime.now(),

    @Enumerated(EnumType.STRING)
    @Column(name = "completed")
    var completed: TodoCompleted = TodoCompleted.FALSE,

    @OneToMany(mappedBy = "todo", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var comments: MutableList<Comment> = mutableListOf(),
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}

fun Todo.toResponse(): TodoResponse {
    return TodoResponse(id!!, title, content, writer, createAt, completed, comments.map { it.toResponse() })
}

fun Todo.toMultiResponse(): TodoListResponse {
    return TodoListResponse(id!!, title, content, writer, createAt, completed)
}