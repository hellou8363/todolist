package org.zerock.todolist

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy

@EnableAspectJAutoProxy
@SpringBootApplication
class TodolistApplication

fun main(args: Array<String>) {
    runApplication<TodolistApplication>(*args)
}
