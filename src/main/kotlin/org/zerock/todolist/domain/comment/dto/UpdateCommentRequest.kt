package org.zerock.todolist.domain.comment.dto

data class UpdateCommentRequest(
    val content: String,
    val writer: String,
    val password: String,
)
