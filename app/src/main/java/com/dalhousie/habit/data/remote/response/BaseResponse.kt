package com.dalhousie.habit.data.remote.response

interface BaseResponse<T> {
    val resultType: String
    val data: T
    val message: String
}