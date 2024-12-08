package com.dalhousie.habit.data.remote.request

data class ResetPasswordRequest(
    val email: String,
    val password: String
)
