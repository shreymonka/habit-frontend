package com.dalhousie.habit.data.remote.response

import com.dalhousie.habit.model.User

data class GetUserDataResponse(
    override val resultType: String,
    override val data: User,
    override val message: String
) : BaseResponse<User>
