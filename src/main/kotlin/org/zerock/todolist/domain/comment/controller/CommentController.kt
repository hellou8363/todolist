package org.zerock.todolist.domain.comment.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.zerock.todolist.domain.comment.dto.CommentResponse
import org.zerock.todolist.domain.comment.dto.CreateCommentRequest
import org.zerock.todolist.domain.comment.dto.DeleteCommentRequest
import org.zerock.todolist.domain.comment.dto.UpdateCommentRequest
import org.zerock.todolist.domain.comment.service.CommentService
import org.zerock.todolist.domain.user.service.UserService

@RestController
@RequestMapping("/todos/{todoId}")
class CommentController(
    private val commentService: CommentService,
    private val userService: UserService
) {

    @PostMapping("/comments")
    fun createComment(
        @PathVariable todoId: Long,
        @RequestBody createCommentRequest: CreateCommentRequest
    ): ResponseEntity<CommentResponse> {
        val userEmail = userService.getUserDetails()?.username
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(commentService.createComment(todoId, createCommentRequest, userEmail))
    }

    @PutMapping("/comments/{commentId}")
    fun updateComment(
        @PathVariable todoId: Long,
        @PathVariable commentId: Long,
        @RequestBody updateCommentRequest: UpdateCommentRequest
    ): ResponseEntity<CommentResponse> {
        val userEmail = userService.getUserDetails()?.username
        return ResponseEntity.status(HttpStatus.OK)
            .body(commentService.updateComment(todoId, commentId, updateCommentRequest, userEmail))
    }

    @DeleteMapping("/comments/{commentId}")
    fun deleteComment(
        @PathVariable todoId: Long,
        @PathVariable commentId: Long,
        @RequestBody deleteCommentRequest: DeleteCommentRequest
    ): ResponseEntity<Unit> {
        val userEmail = userService.getUserDetails()?.username
        commentService.deleteComment(todoId, commentId, deleteCommentRequest, userEmail)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}