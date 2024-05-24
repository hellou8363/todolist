package org.zerock.todolist.domain.todo.dto

import jakarta.validation.constraints.Size

data class UpdateTodoRequest(
    @field:Size(min = 1, max = 200, message = "Title must be between 1 and 200")
    val title: String,
    @field:Size(min = 1, max = 1000, message = "Content must be between 1 and 1000")
    val content: String,
    val writer: String,
    val completed: Boolean,
)