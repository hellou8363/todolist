package org.zerock.todolist.domain.todo.dto

data class CreateTodoRequest(
    val title: String,
    val content: String,
    val writer: String,
)