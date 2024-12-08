package com.dalhousie.habit.data.remote.request

data class AddEditHabitRequest(
    val id: String? = null,
    val name: String,
    val schedule: List<String>
)
