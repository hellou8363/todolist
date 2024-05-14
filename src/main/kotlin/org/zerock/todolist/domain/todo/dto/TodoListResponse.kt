package org.zerock.todolist.domain.todo.dto

import org.zerock.todolist.domain.todo.model.TodoCompleted
import java.time.LocalDateTime

data class TodoListResponse(
    val id: Long,
    val title: String,
    val content: String,
    val writer: String,
    val createAt: LocalDateTime,
    val completed: TodoCompleted,
)