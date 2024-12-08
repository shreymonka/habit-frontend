package com.dalhousie.habit.data.remote.response

import com.dalhousie.habit.model.Habit

data class TodayHabitsResponse(
    override val resultType: String,
    override val data: Data,
    override val message: String
) : BaseResponse<TodayHabitsResponse.Data> {

    data class Data(
        val habitAndStatusList: List<HabitAndStatus>
    ) {

        data class HabitAndStatus(
            val habit: Habit,
            val isCompleted: Boolean
        )
    }
}
