package org.zerock.todolist.domain.todo.dto

import org.zerock.todolist.domain.comment.dto.CommentResponse
import org.zerock.todolist.domain.todo.model.TodoCompleted
import java.time.LocalDateTime

data class TodoResponse(
    val id: Long,
    val title: String,
    val content: String,
    val writer: String,
    val createdAt: LocalDateTime,
    val completed: TodoCompleted,
    val comments: List<CommentResponse>,
)