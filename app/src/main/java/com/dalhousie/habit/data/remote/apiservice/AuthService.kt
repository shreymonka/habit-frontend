package com.dalhousie.habit.data.remote.apiservice

import com.dalhousie.habit.data.remote.apiresult.ApiResult
import com.dalhousie.habit.data.remote.request.ForgotPasswordRequest
import com.dalhousie.habit.data.remote.request.LoginRequest
import com.dalhousie.habit.data.remote.request.OtpVerificationRequest
import com.dalhousie.habit.data.remote.request.ResetPasswordRequest
import com.dalhousie.habit.data.remote.request.SignUpRequest
import com.dalhousie.habit.data.remote.response.BooleanResponseBody
import com.dalhousie.habit.data.remote.response.LoginResponse
import com.dalhousie.habit.data.remote.response.SearchUserResponse
import com.dalhousie.habit.util.Urls
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {

    @POST(Urls.LOGIN)
    suspend fun loginUser(@Body loginRequest: LoginRequest): ApiResult<LoginResponse>

    @POST(Urls.REGISTER)
    suspend fun registerUser(@Body request: SignUpRequest): ApiResult<BooleanResponseBody>

    @POST(Urls.RESET_PASSWORD)
    suspend fun resetPassword(@Body resetPasswordRequest: ResetPasswordRequest): ApiResult<BooleanResponseBody>

    @POST(Urls.FORGOT_PASSWORD)
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): ApiResult<BooleanResponseBody>

    @POST(Urls.OTP_VERIFICATION)
    suspend fun otpVerification(@Body request: OtpVerificationRequest): ApiResult<BooleanResponseBody>
}