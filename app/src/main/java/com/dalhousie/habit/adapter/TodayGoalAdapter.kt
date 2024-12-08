package com.dalhousie.habit.adapter

import androidx.databinding.ViewDataBinding
import com.dalhousie.habit.R
import com.dalhousie.habit.databinding.ItemTodayGoalBinding
import com.dalhousie.habit.ui.base.BaseRecyclerAdapter

class TodayGoalAdapter : BaseRecyclerAdapter<String>() {
    override fun getLayoutIdForType(viewType: Int): Int = R.layout.item_today_goal

    override fun areItemsSame(firstItem: String, secondItem: String): Boolean =
        firstItem == secondItem

    override fun setDataForListItem(binding: ViewDataBinding, data: String, position: Int) {
        (binding as ItemTodayGoalBinding).apply {
            showDivider = position == (itemCount - 1)
        }
    }
}