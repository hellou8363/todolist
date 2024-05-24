package org.zerock.todolist.config.auth.util

data class CustomJwtException(
    override val message: String,
): RuntimeException(message)