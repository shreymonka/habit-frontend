package com.dalhousie.habit.ui.home.fragments

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.dalhousie.habit.R
import com.dalhousie.habit.adapter.HabitAdapter
import com.dalhousie.habit.adapter.TodayHabitAdapter
import com.dalhousie.habit.databinding.FragmentHabitBinding
import com.dalhousie.habit.databinding.LayoutMarkHabitAsCompleteDialogBinding
import com.dalhousie.habit.ui.base.BaseFragment
import com.dalhousie.habit.util.extensions.observeEvent
import com.dalhousie.habit.viewmodel.home.HabitViewModel
import com.dalhousie.habit.viewmodel.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HabitFragment : BaseFragment<FragmentHabitBinding, HabitViewModel>() {

    override val viewModel: HabitViewModel by viewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()

    private val todayHabitAdapter by lazy {
        TodayHabitAdapter(
            viewModel::onMarkHabitCompleteClicked
        )
    }
    private val habitAdapter by lazy {
        HabitAdapter(
            viewModel::onEditHabitClick
        )
    }

    override fun getLayoutResId(): Int = R.layout.fragment_habit

    override fun initialize() {
        setupRecyclerViews()
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchHabits()
    }

    override fun setupViewModel() {
        super.setupViewModel()
        viewModel.habits.observe(this) {
            viewModel.isHabitsEmpty.value = it.isEmpty()
            habitAdapter.setItems(it)
        }
        viewModel.fetchTodaysGoal.observeEvent(this) {
            homeViewModel.fetchTodayGoals()
        }
        viewModel.navigateToAddEdit.observeEvent(this) {
            homeViewModel.navigate(it)
        }
        viewModel.showMarkHabitCompleteDialog.observeEvent(this) {
            showMarkHabitCompleteDialog(it)
        }
        homeViewModel.todayGoals.observe(this) {
            viewModel.isTodayGoalEmpty.value = it.isEmpty()
            todayHabitAdapter.setItems(it)
        }
    }

    private fun setupRecyclerViews() {
        binding.rvTodaysGoals.adapter = todayHabitAdapter
        binding.rvHabits.adapter = habitAdapter
    }

    private fun showMarkHabitCompleteDialog(habitId: String) {
        val dialog = AlertDialog.Builder(requireContext()).create()
        LayoutMarkHabitAsCompleteDialogBinding.inflate(layoutInflater).apply {
            btnYes.setOnClickListener {
                viewModel.markHabitAsComplete(habitId)
                dialog.dismiss()
            }
            btnCancel.setOnClickListener { dialog.dismiss() }
            dialog.setView(root)
            dialog.show()
        }
    }
}