package org.zerock.todolist.domain.todo.dto

data class UpdateTodoRequest(
    val title: String,
    val content: String,
    val writer: String,
    val completed: Boolean,
)