package org.zerock.todolist.domain.comment.dto

import java.time.LocalDateTime

data class CommentResponse(
    val id: Long,
    val content: String,
    val writer: String,
    val createdAt: LocalDateTime,
)