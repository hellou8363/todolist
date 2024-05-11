package org.zerock.todolist.domain.todo.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class TodoResponse(
    val id: Long,
    val title: String,
    val content: String,
    val writer: String,
    val createAt: LocalDateTime,
)