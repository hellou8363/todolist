package org.zerock.todolist.domain.comment.service

import org.zerock.todolist.domain.comment.dto.CommentResponse
import org.zerock.todolist.domain.comment.dto.CreateAndUpdateCommentRequest
import org.zerock.todolist.domain.comment.dto.DeleteCommentRequest

interface CommentService {

    fun getAllCommentList(): List<CommentResponse>

    fun getCommentById(commentId: Long): CommentResponse

    fun createComment(todoId: Long, request: CreateAndUpdateCommentRequest): CommentResponse

    fun updateComment(todoId: Long, commentId: Long, request: CreateAndUpdateCommentRequest): CommentResponse

    fun deleteComment(todoId: Long, commentId: Long, request: DeleteCommentRequest)
}