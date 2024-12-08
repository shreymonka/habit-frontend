package com.dalhousie.habit.data.remote.request

data class SignUpRequest(
    val userName: String,
    val email: String,
    val password: String
)
