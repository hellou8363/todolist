package org.zerock.todolist.task

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.zerock.todolist.domain.todo.repository.TodoRepository

@Component
class ScheduledDeletionTasks(
    private val todoRepository: TodoRepository
) {

    @Scheduled(cron = "0 0/5 * * * *") // 5분마다 실행
    fun reportCurrentTime() {
        todoRepository.deleteByIsDeletedTrue()
    }
}