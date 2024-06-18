package org.zerock.todolist.exception

data class AlreadyExistsException(
    val email: String
) : RuntimeException("This email already exists. $email")