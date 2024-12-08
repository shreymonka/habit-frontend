package com.dalhousie.habit.data.remote.request

data class OtpVerificationRequest(
    val otp: String,
    val email: String
)