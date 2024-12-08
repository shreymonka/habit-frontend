package com.dalhousie.habit.enums

import com.dalhousie.habit.R
import com.dalhousie.habit.ui.home.fragments.DashboardFragment
import com.dalhousie.habit.ui.home.fragments.HabitFragment
import com.dalhousie.habit.ui.home.fragments.SearchFragment
import com.dalhousie.habit.ui.home.fragments.SettingsFragment

enum class HomeBottomNavigationItem {
    DASHBOARD,
    SEARCH,
    HABIT,
    SETTINGS;

    fun getFragmentId(): Int = when (this) {
        DASHBOARD -> R.id.dashboardFragment
        SEARCH -> R.id.searchFragment
        HABIT -> R.id.habitFragment
        SETTINGS -> R.id.settingsFragment
    }

    companion object {
        fun getItemFromFragmentName(fragmentName: String) = when (fragmentName) {
            DashboardFragment::class.simpleName -> DASHBOARD
            SearchFragment::class.simpleName -> SEARCH
            HabitFragment::class.simpleName -> HABIT
            SettingsFragment::class.simpleName -> SETTINGS
            else -> DASHBOARD
        }
    }
}