package org.zerock.todolist.infra.aop

@Target(AnnotationTarget.FUNCTION) // 어노테이션이 적용될 대상
@Retention(AnnotationRetention.RUNTIME) // 어느 시점까지 사용될 수 있는지
annotation class StopWatch