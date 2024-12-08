package com.dalhousie.habit.util

object Urls {
    private const val AUTH_PREFIX = "/api/auth"
    private const val HABIT_PREFIX = "/api/habit"
    private const val USER_PREFIX = "/api/user"
    const val BASE_URL = "https://habit-backend-jet4.onrender.com"

    // Endpoints
    // Auth
    const val REGISTER = "$AUTH_PREFIX/register"
    const val LOGIN = "$AUTH_PREFIX/login"
    const val FORGOT_PASSWORD = "$AUTH_PREFIX/forgot-password"
    const val OTP_VERIFICATION = "$AUTH_PREFIX/verify-otp"
    const val RESET_PASSWORD = "$AUTH_PREFIX/reset-password"

    // Habit
    const val ADD_HABIT = "$HABIT_PREFIX/add-habit"
    const val GET_HABITS = "$HABIT_PREFIX/get-habits"
    const val EDIT_HABIT = "$HABIT_PREFIX/edit-habit"
    const val DELETE_HABIT = "$HABIT_PREFIX/delete-habit"
    const val GET_TODAY_HABITS = "$HABIT_PREFIX/get-today-habits"
    const val MARK_HABIT_AS_COMPLETE = "$HABIT_PREFIX/mark-habit-as-complete"
    const val HABIT_STATISTICS = "$HABIT_PREFIX/habit-statistics"

    // User
    const val GET_USER_DATA = USER_PREFIX
    const val SEARCH_USERS = "$USER_PREFIX/search"
    const val UPDATE_USERNAME = "$USER_PREFIX/update-username"
    const val UPDATE_PASSWORD = "$USER_PREFIX/update-password"
    const val UPDATE_PROFILE_PIC = "$USER_PREFIX/update-profile-pic"
}