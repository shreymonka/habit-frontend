package com.dalhousie.habit.data.remote.apiservice

import com.dalhousie.habit.data.remote.apiresult.ApiResult
import com.dalhousie.habit.data.remote.request.UpdatePasswordRequest
import com.dalhousie.habit.data.remote.request.UpdateProfilePicRequest
import com.dalhousie.habit.data.remote.request.UpdateUsernameRequest
import com.dalhousie.habit.data.remote.response.BooleanResponseBody
import com.dalhousie.habit.data.remote.response.GetUserDataResponse
import com.dalhousie.habit.data.remote.response.SearchUserResponse
import com.dalhousie.habit.util.Urls
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {

    @GET(Urls.GET_USER_DATA)
    suspend fun getUserData(): ApiResult<GetUserDataResponse>

    @GET(Urls.SEARCH_USERS)
    suspend fun searchUsers(@Query("query") query: String): ApiResult<SearchUserResponse>

    @POST(Urls.UPDATE_USERNAME)
    suspend fun updateUsername(@Body request: UpdateUsernameRequest): ApiResult<BooleanResponseBody>

    @POST(Urls.UPDATE_PASSWORD)
    suspend fun updatePassword(@Body request: UpdatePasswordRequest): ApiResult<BooleanResponseBody>

    @POST(Urls.UPDATE_PROFILE_PIC)
    suspend fun updateProfilePic(@Body request: UpdateProfilePicRequest): ApiResult<BooleanResponseBody>
}