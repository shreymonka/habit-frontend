package com.dalhousie.habit.data.remote.response

import com.dalhousie.habit.model.User

data class SearchUserResponse(
    override val resultType: String,
    override val data: Data,
    override val message: String
) : BaseResponse<SearchUserResponse.Data> {

    data class Data(
        val users: List<User>
    )
}
