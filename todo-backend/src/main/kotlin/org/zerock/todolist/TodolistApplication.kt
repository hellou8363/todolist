package org.zerock.todolist

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling // 스케줄링 활성화
@EnableAspectJAutoProxy
@SpringBootApplication
class TodolistApplication

fun main(args: Array<String>) {
    runApplication<TodolistApplication>(*args)
}
