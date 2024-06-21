package org.zerock.todolist.task

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.zerock.todolist.domain.todo.repository.TodoRepository
import org.zerock.todolist.infra.aop.StopWatch

@Component
class ScheduledDeletionTasks(
    private val todoRepository: TodoRepository
) {
    @StopWatch
    @Scheduled(cron = "0 0/5 * * * *") // 5분마다 실행
    fun reportCurrentTime() {
        todoRepository.deleteByIsDeletedTrue()
    }
}