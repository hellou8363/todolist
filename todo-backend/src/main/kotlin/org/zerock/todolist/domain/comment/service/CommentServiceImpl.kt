package org.zerock.todolist.domain.comment.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.zerock.todolist.domain.comment.dto.CommentResponse
import org.zerock.todolist.domain.comment.dto.CreateAndUpdateCommentRequest
import org.zerock.todolist.domain.comment.dto.DeleteCommentRequest
import org.zerock.todolist.domain.comment.model.Comment
import org.zerock.todolist.domain.comment.model.toResponse
import org.zerock.todolist.domain.comment.repository.CommentRepository
import org.zerock.todolist.domain.todo.repository.TodoRepository
import org.zerock.todolist.domain.user.repository.UserRepository
import org.zerock.todolist.exception.CustomAccessDeniedException
import org.zerock.todolist.exception.ModelNotFoundException

@Service
class CommentServiceImpl(
    private val commentRepository: CommentRepository,
    private val todoRepository: TodoRepository,
    private val userRepository: UserRepository
) : CommentService {
    override fun getAllCommentList(): List<CommentResponse> {
        return commentRepository.findAll().map { it.toResponse() }.sortedBy { it.createdAt }.reversed()
    }

    override fun getCommentById(commentId: Long): CommentResponse {
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw ModelNotFoundException("Comment", commentId)
        return comment.toResponse()
    }

    @Transactional
    override fun createComment(
        todoId: Long,
        userId: Long,
        request: CreateAndUpdateCommentRequest
    ): CommentResponse {
        val user = userRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("User", userId)
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo", todoId)

        return commentRepository.save(
            Comment.from(
                request,
                todo = todo,
                user = user
            )
        ).toResponse()
    }

    @Transactional
    override fun updateComment(
        todoId: Long,
        userId: Long,
        commentId: Long,
        request: CreateAndUpdateCommentRequest
    ): CommentResponse {
        val comment = commentRepository.findByTodoIdAndId(todoId, commentId)
            ?: throw ModelNotFoundException("Comment", commentId)

        if (comment.user.id != userId) {
            throw CustomAccessDeniedException("You do not have access.")
        }

        if (comment.writer == request.writer && comment.password == request.password) {
            comment.update(content = request.content, writer = request.writer)

            return comment.toResponse()
        } else {
            throw IllegalArgumentException("writer or password does not match.")
        }
    }

    @Transactional
    override fun deleteComment(todoId: Long, commentId: Long, userId: Long, request: DeleteCommentRequest) {
        val comment =
            commentRepository.findByTodoIdAndId(todoId, commentId) ?: throw ModelNotFoundException("Comment", commentId)

        if (comment.user.id != userId) {
            throw CustomAccessDeniedException("You do not have access.")
        }

        if (comment.writer == request.writer && comment.password == request.password) {
            return commentRepository.delete(comment)
        } else {
            throw IllegalArgumentException("writer or password does not match.")
        }
    }
}