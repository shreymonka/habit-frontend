package com.dalhousie.habit.data.remote.request

data class UpdatePasswordRequest(
    val oldPassword: String,
    val newPassword: String
)
