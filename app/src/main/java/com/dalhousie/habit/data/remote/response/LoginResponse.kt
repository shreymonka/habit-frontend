package com.dalhousie.habit.data.remote.response

data class LoginResponse(
    override val resultType: String,
    override val data: Data,
    override val message: String
) : BaseResponse<LoginResponse.Data> {

    data class Data(
        val token: String
    )
}
