package com.leoapps.waterapp.common.domain.task_result

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

sealed class TaskResult<out T> {
    object Loading : TaskResult<Nothing>()

    data class Success<T>(
        val value: T
    ) : TaskResult<T>()

    data class Failure(
        val error: Throwable
    ) : TaskResult<Nothing>()
}

@OptIn(ExperimentalContracts::class)
fun <T> TaskResult<T>?.isSuccess(): Boolean {
    contract {
        returns(true) implies (this@isSuccess != null && this@isSuccess is TaskResult.Success<T>)
    }
    return this is TaskResult.Success
}

@OptIn(ExperimentalContracts::class)
fun TaskResult<*>?.isFailure(): Boolean {
    contract {
        returns(true) implies (this@isFailure != null && this@isFailure is TaskResult.Failure)
    }
    return this is TaskResult.Failure
}

inline fun <R, T> TaskResult<T>.map(transform: (value: T) -> R): TaskResult<R> {
    return when {
        isSuccess() -> TaskResult.Success(transform(value))
        else -> this as TaskResult.Failure
    }
}

fun <T> TaskResult<T>.mapToUnit(): TaskResult<Unit> {
    return this.map { Unit }
}

fun <T> Flow<TaskResult<T>>.onSuccess(
    callback: suspend (T) -> Unit
): Flow<TaskResult<T>> {
    return this.onEach { result ->
        if (result.isSuccess()) {
            callback.invoke(result.value)
        }
    }
}