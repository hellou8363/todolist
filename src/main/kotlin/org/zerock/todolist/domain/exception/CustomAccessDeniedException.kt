package org.zerock.todolist.domain.exception

data class CustomAccessDeniedException (
    override val message: String,
): RuntimeException(message)