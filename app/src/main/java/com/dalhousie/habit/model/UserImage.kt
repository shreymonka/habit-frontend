package com.dalhousie.habit.model

import com.dalhousie.habit.R

data class UserImage(
    val id: Int,
    val image: Int,
    val isSelected: Boolean
) {
    companion object {
        fun getUserImageList(selectedIndex: Int) = buildList {
            add(UserImage(0, R.drawable.ic_profile_0, selectedIndex == 0))
            add(UserImage(1, R.drawable.ic_profile_1, selectedIndex == 1))
            add(UserImage(2, R.drawable.ic_profile_2, selectedIndex == 2))
            add(UserImage(3, R.drawable.ic_profile_3, selectedIndex == 3))
            add(UserImage(4, R.drawable.ic_profile_4, selectedIndex == 4))
            add(UserImage(5, R.drawable.ic_profile_5, selectedIndex == 5))
            add(UserImage(6, R.drawable.ic_profile_6, selectedIndex == 6))
            add(UserImage(7, R.drawable.ic_profile_7, selectedIndex == 7))
            add(UserImage(8, R.drawable.ic_profile_8, selectedIndex == 8))
            add(UserImage(9, R.drawable.ic_profile_9, selectedIndex == 9))
        }
    }
}
