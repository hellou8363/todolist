package org.zerock.todolist.domain.comment.dto

data class DeleteCommentRequest(
    val writer: String,
    val password: String,
)
