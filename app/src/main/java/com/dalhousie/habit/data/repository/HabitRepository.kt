package com.dalhousie.habit.data.repository

import com.dalhousie.habit.data.remote.apiresult.ApiResult
import com.dalhousie.habit.data.remote.apiservice.HabitService
import com.dalhousie.habit.data.remote.request.AddEditHabitRequest
import com.dalhousie.habit.data.remote.request.MarkHabitAsCompleteRequest
import com.dalhousie.habit.data.remote.response.BooleanResponseBody
import com.dalhousie.habit.data.remote.response.GetHabitsResponse
import com.dalhousie.habit.data.remote.response.HabitStatisticsResponse
import com.dalhousie.habit.data.remote.response.SingleHabitResponse
import com.dalhousie.habit.data.remote.response.TodayHabitsResponse
import javax.inject.Inject

interface HabitRepository {

    suspend fun addHabit(request: AddEditHabitRequest): ApiResult<SingleHabitResponse>

    suspend fun getHabits(): ApiResult<GetHabitsResponse>

    suspend fun editHabit(request: AddEditHabitRequest): ApiResult<SingleHabitResponse>

    suspend fun deleteHabit(id: String): ApiResult<BooleanResponseBody>

    suspend fun getTodayHabits(): ApiResult<TodayHabitsResponse>

    suspend fun markHabitAsComplete(request: MarkHabitAsCompleteRequest): ApiResult<BooleanResponseBody>

    suspend fun getHabitStatistics(): ApiResult<HabitStatisticsResponse>
}

class HabitRepositoryImpl @Inject constructor(
    private val habitService: HabitService
) : HabitRepository {

    override suspend fun addHabit(request: AddEditHabitRequest): ApiResult<SingleHabitResponse> =
        habitService.addHabit(request)

    override suspend fun getHabits(): ApiResult<GetHabitsResponse> =
        habitService.getHabits()

    override suspend fun editHabit(request: AddEditHabitRequest): ApiResult<SingleHabitResponse> =
        habitService.editHabit(request)

    override suspend fun deleteHabit(id: String): ApiResult<BooleanResponseBody> =
        habitService.deleteHabit(id)

    override suspend fun getTodayHabits(): ApiResult<TodayHabitsResponse> =
        habitService.getTodayHabits()

    override suspend fun markHabitAsComplete(request: MarkHabitAsCompleteRequest): ApiResult<BooleanResponseBody> =
        habitService.markHabitAsComplete(request)

    override suspend fun getHabitStatistics(): ApiResult<HabitStatisticsResponse> =
        habitService.getHabitStatistics()
}