package com.dalhousie.habit.data.remote.apiservice

import com.dalhousie.habit.data.remote.apiresult.ApiResult
import com.dalhousie.habit.data.remote.request.AddEditHabitRequest
import com.dalhousie.habit.data.remote.request.MarkHabitAsCompleteRequest
import com.dalhousie.habit.data.remote.response.BooleanResponseBody
import com.dalhousie.habit.data.remote.response.GetHabitsResponse
import com.dalhousie.habit.data.remote.response.HabitStatisticsResponse
import com.dalhousie.habit.data.remote.response.SearchUserResponse
import com.dalhousie.habit.data.remote.response.SingleHabitResponse
import com.dalhousie.habit.data.remote.response.TodayHabitsResponse
import com.dalhousie.habit.util.Urls
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface HabitService {

    @POST(Urls.ADD_HABIT)
    suspend fun addHabit(@Body request: AddEditHabitRequest): ApiResult<SingleHabitResponse>

    @GET(Urls.GET_HABITS)
    suspend fun getHabits(): ApiResult<GetHabitsResponse>

    @POST(Urls.EDIT_HABIT)
    suspend fun editHabit(@Body request: AddEditHabitRequest): ApiResult<SingleHabitResponse>

    @DELETE(Urls.DELETE_HABIT)
    suspend fun deleteHabit(@Query("id") id: String): ApiResult<BooleanResponseBody>

    @GET(Urls.GET_TODAY_HABITS)
    suspend fun getTodayHabits(): ApiResult<TodayHabitsResponse>

    @POST(Urls.MARK_HABIT_AS_COMPLETE)
    suspend fun markHabitAsComplete(@Body request: MarkHabitAsCompleteRequest): ApiResult<BooleanResponseBody>

    @GET(Urls.HABIT_STATISTICS)
    suspend fun getHabitStatistics(): ApiResult<HabitStatisticsResponse>
}