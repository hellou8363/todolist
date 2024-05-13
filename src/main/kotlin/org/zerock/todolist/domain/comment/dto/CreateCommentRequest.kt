package org.zerock.todolist.domain.comment.dto

data class CreateCommentRequest(
    val content: String,
    val writer: String,
    val password: String,
)
