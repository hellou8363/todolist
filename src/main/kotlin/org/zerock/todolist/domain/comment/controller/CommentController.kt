package org.zerock.todolist.domain.comment.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.zerock.todolist.domain.comment.dto.CommentResponse
import org.zerock.todolist.domain.comment.dto.CreateCommentRequest
import org.zerock.todolist.domain.comment.dto.DeleteCommentRequest
import org.zerock.todolist.domain.comment.dto.UpdateCommentRequest
import org.zerock.todolist.domain.comment.service.CommentService

@RestController
@RequestMapping("/todos/{todoId}")
class CommentController(
    private val commentService: CommentService
) {

    @PostMapping("/comments")
    fun createComment(
        @PathVariable todoId: Long,
        @RequestBody createCommentRequest: CreateCommentRequest
    ): ResponseEntity<CommentResponse> {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(commentService.createComment(todoId, createCommentRequest))
    }

    @PutMapping("/comments/{commentId}")
    fun updateComment(
        @PathVariable todoId: Long,
        @PathVariable commentId: Long,
        @RequestBody updateCommentRequest: UpdateCommentRequest
    ): ResponseEntity<CommentResponse> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(commentService.updateComment(todoId, commentId, updateCommentRequest))
    }

    @DeleteMapping("/comments/{commentId}")
    fun deleteComment(
        @PathVariable todoId: Long,
        @PathVariable commentId: Long,
        @RequestBody deleteCommentRequest: DeleteCommentRequest
    ): ResponseEntity<Unit> {
        commentService.deleteComment(todoId, commentId, deleteCommentRequest)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}