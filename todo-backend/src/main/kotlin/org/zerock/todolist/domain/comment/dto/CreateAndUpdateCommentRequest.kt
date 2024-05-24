package org.zerock.todolist.domain.comment.dto

data class CreateAndUpdateCommentRequest(
    val content: String,
    val writer: String,
    val password: String,
)
