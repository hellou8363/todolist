package org.zerock.todolist.domain.exception

data class AlreadyExistsException(
    val email: String
) : RuntimeException("This email already exists. $email")