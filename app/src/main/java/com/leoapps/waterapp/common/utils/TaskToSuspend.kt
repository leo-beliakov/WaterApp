package com.leoapps.waterapp.common.utils

import com.google.android.gms.tasks.Task
import com.leoapps.waterapp.common.domain.task_result.TaskResult
import kotlinx.coroutines.suspendCancellableCoroutine

suspend fun <T> Task<T>.asSuspend() = suspendCancellableCoroutine<TaskResult<T>> { continuation ->
    this.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val result = TaskResult.Success(task.result)
            continuation.resumeWith(Result.success(result))
        } else {
            val exception = task.exception ?: UnknownError("Task failed")
            val result = TaskResult.Failure(exception)
            continuation.resumeWith(Result.success(result))
        }
    }
}