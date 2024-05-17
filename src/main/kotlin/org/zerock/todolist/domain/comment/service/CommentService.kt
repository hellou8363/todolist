package org.zerock.todolist.domain.comment.service

import org.zerock.todolist.domain.comment.dto.CommentResponse
import org.zerock.todolist.domain.comment.dto.CreateCommentRequest
import org.zerock.todolist.domain.comment.dto.DeleteCommentRequest
import org.zerock.todolist.domain.comment.dto.UpdateCommentRequest

interface CommentService {

    fun getAllCommentList(): List<CommentResponse>

    fun getCommentById(commentId: Long): CommentResponse

    fun createComment(todoId: Long, request: CreateCommentRequest, userEmail: String?): CommentResponse

    fun updateComment(todoId: Long, commentId: Long, request: UpdateCommentRequest, userEmail: String?): CommentResponse

    fun deleteComment(todoId: Long, commentId: Long, request: DeleteCommentRequest, userEmail: String?)
}