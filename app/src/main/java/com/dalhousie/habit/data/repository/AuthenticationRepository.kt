package com.dalhousie.habit.data.repository

import com.dalhousie.habit.data.remote.apiresult.ApiResult
import com.dalhousie.habit.data.remote.apiservice.AuthService
import com.dalhousie.habit.data.remote.request.ForgotPasswordRequest
import com.dalhousie.habit.data.remote.request.LoginRequest
import com.dalhousie.habit.data.remote.request.OtpVerificationRequest
import com.dalhousie.habit.data.remote.request.ResetPasswordRequest
import com.dalhousie.habit.data.remote.request.SignUpRequest
import com.dalhousie.habit.data.remote.response.BooleanResponseBody
import com.dalhousie.habit.data.remote.response.LoginResponse
import com.dalhousie.habit.data.remote.response.SearchUserResponse
import javax.inject.Inject

interface AuthenticationRepository {

    suspend fun loginUser(request: LoginRequest): ApiResult<LoginResponse>

    suspend fun registerUser(request: SignUpRequest): ApiResult<BooleanResponseBody>

    suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest): ApiResult<BooleanResponseBody>

    suspend fun forgotPassword(request: ForgotPasswordRequest): ApiResult<BooleanResponseBody>

    suspend fun otpVerification(request: OtpVerificationRequest): ApiResult<BooleanResponseBody>

}

class AuthenticationRepositoryImpl @Inject constructor(
    private val authService: AuthService
) : AuthenticationRepository {

    override suspend fun loginUser(request: LoginRequest): ApiResult<LoginResponse> {
        return authService.loginUser(request)
    }

    override suspend fun registerUser(request: SignUpRequest): ApiResult<BooleanResponseBody> {
        return authService.registerUser(request)
    }

    override suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest): ApiResult<BooleanResponseBody> {
        return authService.resetPassword(resetPasswordRequest)
    }

    override suspend fun forgotPassword(request: ForgotPasswordRequest): ApiResult<BooleanResponseBody> {
        return authService.forgotPassword(request)
    }

    override suspend fun otpVerification(request: OtpVerificationRequest): ApiResult<BooleanResponseBody> {
        return authService.otpVerification(request)
    }

}