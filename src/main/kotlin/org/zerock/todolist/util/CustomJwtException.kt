package org.zerock.todolist.util

data class CustomJwtException(
    override val message: String,
): RuntimeException(message)