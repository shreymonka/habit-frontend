package com.dalhousie.habit.adapter

import androidx.databinding.ViewDataBinding
import com.dalhousie.habit.R
import com.dalhousie.habit.databinding.ItemHabitBinding
import com.dalhousie.habit.model.Habit
import com.dalhousie.habit.ui.base.BaseRecyclerAdapter

class UserHabitAdapter(
    private val onHabitClicked: (Habit) -> Unit
) : BaseRecyclerAdapter<Habit>() {

    override fun getLayoutIdForType(viewType: Int): Int = R.layout.item_habit

    override fun areItemsSame(firstItem: Habit, secondItem: Habit): Boolean =
        firstItem.id == secondItem.id

    override fun setDataForListItem(binding: ViewDataBinding, data: Habit, position: Int) {
        (binding as? ItemHabitBinding)?.apply {
            showEditIcon = false
            root.setOnClickListener {
                onHabitClicked(data)
            }
        }
    }
}