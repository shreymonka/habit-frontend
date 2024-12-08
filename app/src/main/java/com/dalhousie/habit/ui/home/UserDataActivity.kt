package com.dalhousie.habit.ui.home

import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.dalhousie.habit.R
import com.dalhousie.habit.adapter.UserHabitAdapter
import com.dalhousie.habit.databinding.ActivityUserDataBinding
import com.dalhousie.habit.databinding.LayoutHabitDialogBinding
import com.dalhousie.habit.model.Habit
import com.dalhousie.habit.model.User
import com.dalhousie.habit.ui.base.BaseAppCompatActivity
import com.dalhousie.habit.util.extensions.observeEvent
import com.dalhousie.habit.util.extensions.parcelable
import com.dalhousie.habit.viewmodel.home.UserDataViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDataActivity : BaseAppCompatActivity<ActivityUserDataBinding, UserDataViewModel>() {

    override val viewModel: UserDataViewModel by viewModels()

    private val userHabitAdapter by lazy {
        UserHabitAdapter(viewModel::showHabitDetailsDialog)
    }

    override fun getLayoutResId(): Int = R.layout.activity_user_data

    override fun initialize() {
        binding.rvHabits.adapter = userHabitAdapter
        intent.extras?.let {
            it.parcelable<User>(USER_KEY)?.let { user ->
                viewModel.setUserData(user)
            }
        }
    }

    override fun setupViewModel() {
        super.setupViewModel()
        viewModel.onBackClick.observeEvent(this) {
            onBackPressedDispatcher.onBackPressed()
        }
        viewModel.userData.observe(this) {
            userHabitAdapter.setItems(it.habits)
        }
        viewModel.showHabitDialog.observeEvent(this) {
            showHabitDialog(it, viewModel::copyHabit)
        }
    }

    private fun showHabitDialog(habit: Habit, onCopy: (Habit) -> Unit) {
        val alertDialog = AlertDialog.Builder(this).create()
        LayoutHabitDialogBinding.inflate(layoutInflater).apply {
            this.habit = habit
            btnCopy.setOnClickListener { onCopy(habit) }
            btnCancel.setOnClickListener { alertDialog.dismiss() }
            alertDialog.setView(root)
            alertDialog.show()
        }
    }

    companion object {
        const val USER_KEY = "USER"
    }
}