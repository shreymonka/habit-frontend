package com.dalhousie.habit.data.repository

import com.dalhousie.habit.data.remote.apiresult.ApiResult
import com.dalhousie.habit.data.remote.apiservice.UserService
import com.dalhousie.habit.data.remote.request.UpdatePasswordRequest
import com.dalhousie.habit.data.remote.request.UpdateProfilePicRequest
import com.dalhousie.habit.data.remote.request.UpdateUsernameRequest
import com.dalhousie.habit.data.remote.response.BooleanResponseBody
import com.dalhousie.habit.data.remote.response.GetUserDataResponse
import com.dalhousie.habit.data.remote.response.SearchUserResponse
import javax.inject.Inject
import javax.inject.Singleton

interface UserRepository {

    suspend fun getUserData(): ApiResult<GetUserDataResponse>

    suspend fun searchUsers(query: String): ApiResult<SearchUserResponse>

    suspend fun updateUsername(request: UpdateUsernameRequest): ApiResult<BooleanResponseBody>

    suspend fun updatePassword(request: UpdatePasswordRequest): ApiResult<BooleanResponseBody>

    suspend fun updateProfilePic(request: UpdateProfilePicRequest): ApiResult<BooleanResponseBody>
}

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userService: UserService
) : UserRepository {

    override suspend fun getUserData(): ApiResult<GetUserDataResponse> =
        userService.getUserData()

    override suspend fun searchUsers(query: String): ApiResult<SearchUserResponse> =
        userService.searchUsers(query)

    override suspend fun updateUsername(request: UpdateUsernameRequest): ApiResult<BooleanResponseBody> =
        userService.updateUsername(request)

    override suspend fun updatePassword(request: UpdatePasswordRequest): ApiResult<BooleanResponseBody> =
        userService.updatePassword(request)

    override suspend fun updateProfilePic(request: UpdateProfilePicRequest): ApiResult<BooleanResponseBody> =
        userService.updateProfilePic(request)
}