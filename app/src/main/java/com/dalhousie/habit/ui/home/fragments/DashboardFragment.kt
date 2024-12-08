package com.dalhousie.habit.ui.home.fragments

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.dalhousie.habit.R
import com.dalhousie.habit.adapter.HabitStatisticsAdapter
import com.dalhousie.habit.adapter.TodayGoalAdapter
import com.dalhousie.habit.databinding.FragmentDashboardBinding
import com.dalhousie.habit.ui.base.BaseFragment
import com.dalhousie.habit.viewmodel.home.DashboardViewModel
import com.dalhousie.habit.viewmodel.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : BaseFragment<FragmentDashboardBinding, DashboardViewModel>() {

    override val viewModel: DashboardViewModel by viewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()

    private val todayGoalAdapter by lazy { TodayGoalAdapter() }
    private val habitStatisticsAdapter by lazy { HabitStatisticsAdapter() }

    override fun getLayoutResId(): Int = R.layout.fragment_dashboard

    override fun initialize() {
        binding.rvTodayGoal.adapter = todayGoalAdapter
        binding.rvHabitStatistics.adapter = habitStatisticsAdapter
        binding.homeViewModel = homeViewModel
    }

    override fun setupViewModel() {
        super.setupViewModel()
        homeViewModel.todayGoalString.observe(this) {
            todayGoalAdapter.setItems(it)
        }
        viewModel.currentMonthYearAndStatistics.observe(this) {
            // Dummy Observer for other dependant live data's to work
        }
        viewModel.currentHabitStatisticsData.observe(this) {
            habitStatisticsAdapter.setItems(it)
        }
        homeViewModel.loggedInUserData.observe(this) {
            viewModel.setLoggedInUserName(it.userName)
        }
    }
}
