package com.dalhousie.habit.ui.home

import androidx.activity.viewModels
import com.dalhousie.habit.R
import com.dalhousie.habit.databinding.ActivityAddEditHabitBinding
import com.dalhousie.habit.model.Habit
import com.dalhousie.habit.ui.base.BaseAppCompatActivity
import com.dalhousie.habit.util.extensions.observeEvent
import com.dalhousie.habit.util.extensions.parcelable
import com.dalhousie.habit.viewmodel.home.AddEditHabitViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditHabitActivity :
    BaseAppCompatActivity<ActivityAddEditHabitBinding, AddEditHabitViewModel>() {
    override val viewModel: AddEditHabitViewModel by viewModels()

    override fun getLayoutResId(): Int = R.layout.activity_add_edit_habit

    override fun initialize() {
        intent.extras?.let {
            it.parcelable<Habit>(HABIT)?.let { habit ->
                viewModel.setHabit(habit)
            }
            it.getBoolean(IS_EDIT_MODE, false).let {
                viewModel.setEditMode(it)
            }
        }
    }

    override fun setupViewModel() {
        super.setupViewModel()
        viewModel.navigationEvent.observeEvent(this) { navigation ->
            when (navigation) {
                AddEditHabitViewModel.Navigation.Back -> finish()
            }
        }
    }

    companion object {
        const val IS_EDIT_MODE = "IS_EDIT_MODE"
        const val HABIT = "HABIT"
    }
}