package com.dalhousie.habit.adapter

import com.dalhousie.habit.R
import com.dalhousie.habit.ui.base.BaseRecyclerAdapter

class HabitStatisticsAdapter : BaseRecyclerAdapter<Float>() {

    override fun getLayoutIdForType(viewType: Int): Int = R.layout.item_habit_statistics

    override fun areItemsSame(firstItem: Float, secondItem: Float): Boolean =
        firstItem == secondItem
}