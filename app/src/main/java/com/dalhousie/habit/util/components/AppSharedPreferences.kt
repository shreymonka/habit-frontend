package com.dalhousie.habit.util.components

import android.content.Context
import androidx.core.content.edit
import com.dalhousie.habit.R
import com.dalhousie.habit.util.Constants.AUTH_TOKEN_KEY
import com.dalhousie.habit.util.Constants.REMEMBERED_EMAIL_KEY
import com.dalhousie.habit.util.Constants.USER_LOGGED_IN_KEY
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

interface AppSharedPreferences {

    fun isUserLoggedIn(): Boolean

    fun setUserLoggedIn(isLoggedIn: Boolean)

    fun getAuthToken(): String?

    fun setAuthToken(token: String)

    fun getRememberedEmail(): String?

    fun setRememberedEmail(email: String)
}

@Singleton
class AppSharedPreferencesImpl @Inject constructor(
    @ApplicationContext private val context: Context
): AppSharedPreferences {
    private val prefs =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    override fun isUserLoggedIn(): Boolean =
        prefs.getBoolean(USER_LOGGED_IN_KEY, false)

    override fun setUserLoggedIn(isLoggedIn: Boolean) {
        prefs.edit { putBoolean(USER_LOGGED_IN_KEY, isLoggedIn) }
    }

    override fun getAuthToken(): String? =
        prefs.getString(AUTH_TOKEN_KEY, null)

    override fun setAuthToken(token: String) {
        prefs.edit { putString(AUTH_TOKEN_KEY, token) }
    }

    override fun getRememberedEmail(): String? =
        prefs.getString(REMEMBERED_EMAIL_KEY, null)

    override fun setRememberedEmail(email: String) {
        prefs.edit { putString(REMEMBERED_EMAIL_KEY, email) }
    }
}