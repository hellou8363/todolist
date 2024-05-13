package org.zerock.todolist.domain.comment.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.zerock.todolist.domain.comment.model.Comment

interface CommentRepository: JpaRepository<Comment, Long> {

    fun findByTodoIdAndId(todoId: Long, commentId: Long): Comment?
}