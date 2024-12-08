package com.dalhousie.habit.ui.home_bottom_navigation

import com.dalhousie.habit.enums.HomeBottomNavigationItem

fun interface HomeBottomNavigationItemClickListener {
    fun onItemClicked(item: HomeBottomNavigationItem)
}