package com.dalhousie.habit.model

import android.os.Parcelable
import com.dalhousie.habit.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String,
    val profilePicId: Int,
    val userName: String,
    val email: String,
    val habits: List<Habit>
) : Parcelable {

    val profileImage: Int
        get() = when (profilePicId) {
            0 -> R.drawable.ic_profile_0
            1 -> R.drawable.ic_profile_1
            2 -> R.drawable.ic_profile_2
            3 -> R.drawable.ic_profile_3
            4 -> R.drawable.ic_profile_4
            5 -> R.drawable.ic_profile_5
            6 -> R.drawable.ic_profile_6
            7 -> R.drawable.ic_profile_7
            8 -> R.drawable.ic_profile_8
            9 -> R.drawable.ic_profile_9
            else -> R.drawable.ic_profile_0
        }
}
