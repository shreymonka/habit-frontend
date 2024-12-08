package com.dalhousie.habit.data.remote.response

import com.dalhousie.habit.model.Habit

data class SingleHabitResponse(
    override val resultType: String,
    override val data: Habit,
    override val message: String
) : BaseResponse<Habit>
