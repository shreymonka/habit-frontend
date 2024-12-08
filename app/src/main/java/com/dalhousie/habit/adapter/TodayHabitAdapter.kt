package com.dalhousie.habit.adapter

import android.graphics.Paint
import androidx.databinding.ViewDataBinding
import com.dalhousie.habit.R
import com.dalhousie.habit.data.remote.response.TodayHabitsResponse
import com.dalhousie.habit.databinding.ItemHabitTodaysGoalBinding
import com.dalhousie.habit.ui.base.BaseRecyclerAdapter

class TodayHabitAdapter(
    private val onHabitCheckChanged: (TodayHabitsResponse.Data.HabitAndStatus) -> Unit
) : BaseRecyclerAdapter<TodayHabitsResponse.Data.HabitAndStatus>() {

    override fun getLayoutIdForType(viewType: Int): Int = R.layout.item_habit_todays_goal

    override fun areItemsSame(
        firstItem: TodayHabitsResponse.Data.HabitAndStatus,
        secondItem: TodayHabitsResponse.Data.HabitAndStatus
    ): Boolean = firstItem.habit.id == secondItem.habit.id

    override fun setDataForListItem(
        binding: ViewDataBinding,
        data: TodayHabitsResponse.Data.HabitAndStatus,
        position: Int
    ) {
        (binding as? ItemHabitTodaysGoalBinding)?.apply {
            vwCheckboxWrapper.setOnClickListener {
                onHabitCheckChanged(data)
            }
            if (data.isCompleted)
                tvHabitName.apply { paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG }
        }
    }
}