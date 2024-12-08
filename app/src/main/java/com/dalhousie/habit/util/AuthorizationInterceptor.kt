package com.dalhousie.habit.util

import com.dalhousie.habit.util.Constants.AUTH_TOKEN_HEADER_NAME
import com.dalhousie.habit.util.components.AppSharedPreferences
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

interface AuthorizationInterceptor : Interceptor

@Singleton
class AuthorizationInterceptorImpl @Inject constructor(
    private val appSharedPreferences: AppSharedPreferences
): AuthorizationInterceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().apply {
            appSharedPreferences.getAuthToken()?.let {
                addHeader(AUTH_TOKEN_HEADER_NAME, "Bearer $it")
            }
        }.build()
        return chain.proceed(request)
    }
}