package com.dalhousie.habit.data.remote.response

import com.dalhousie.habit.model.Habit

data class GetHabitsResponse(
    override val resultType: String,
    override val data: Data,
    override val message: String
) : BaseResponse<GetHabitsResponse.Data> {

    data class Data(
        val habits: List<Habit>
    )
}
