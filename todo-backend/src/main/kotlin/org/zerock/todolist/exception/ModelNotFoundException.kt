package org.zerock.todolist.exception

data class ModelNotFoundException(
    val modelName: String,
    val id: Long?,
) : RuntimeException(if (id != null) "Model $modelName not found with given id: $id" else "Model $modelName not found")