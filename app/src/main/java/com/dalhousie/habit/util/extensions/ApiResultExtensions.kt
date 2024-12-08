package com.dalhousie.habit.util.extensions

import com.dalhousie.habit.data.remote.apiresult.ApiError
import com.dalhousie.habit.data.remote.apiresult.ApiException
import com.dalhousie.habit.data.remote.apiresult.ApiResult
import com.dalhousie.habit.data.remote.apiresult.ApiSuccess

suspend fun <T : Any> ApiResult<T>.onSuccess(
    executable: suspend (T) -> Unit
): ApiResult<T> = apply {
    if (this is ApiSuccess<T>) {
        executable(data)
    }
}

suspend fun <T : Any> ApiResult<T>.onError(
    executable: suspend (code: Int, message: String?) -> Unit
): ApiResult<T> = apply {
    if (this is ApiError<T>) {
        executable(code, message)
    }
}

suspend fun <T : Any> ApiResult<T>.onException(
    executable: suspend (e: Throwable) -> Unit
): ApiResult<T> = apply {
    if (this is ApiException<T>) {
        executable(exception)
    }
}

suspend fun <T : Any, R : Any> ApiResult<T>.mapOnSuccess(
    transform: suspend (T) -> R
): ApiResult<R> = when (this) {
    is ApiError -> ApiError(code, message)
    is ApiException -> ApiException(exception)
    is ApiSuccess -> ApiSuccess(transform(data))
}