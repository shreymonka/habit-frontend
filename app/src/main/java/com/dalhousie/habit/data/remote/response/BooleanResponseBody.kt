package com.dalhousie.habit.data.remote.response

data class BooleanResponseBody(
    override val resultType: String,
    override val data: Boolean,
    override val message: String
) : BaseResponse<Boolean>
