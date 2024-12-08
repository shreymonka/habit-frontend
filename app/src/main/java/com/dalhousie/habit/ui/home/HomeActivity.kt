package com.dalhousie.habit.ui.home

import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.dalhousie.habit.R
import com.dalhousie.habit.databinding.ActivityHomeBinding
import com.dalhousie.habit.enums.HomeBottomNavigationItem
import com.dalhousie.habit.ui.authentication.AuthenticationActivity
import com.dalhousie.habit.ui.base.BaseAppCompatActivity
import com.dalhousie.habit.util.extensions.launchActivity
import com.dalhousie.habit.util.extensions.launchActivityAndFinish
import com.dalhousie.habit.util.extensions.observeEvent
import com.dalhousie.habit.viewmodel.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseAppCompatActivity<ActivityHomeBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by viewModels()

    private val navController by lazy { binding.fcvHomeContainer.findNavController() }

    override fun getLayoutResId(): Int = R.layout.activity_home

    override fun initialize() {
        onBackPressedDispatcher.addCallback(this) {
            onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchTodayGoals()
    }

    override fun setupViewModel() {
        viewModel.currentItemSelected.observeEvent(this) {
            navController.navigate(it.getFragmentId(), null, navController.getHomeNavOptions())
        }
        viewModel.navigateToAddEdit.observeEvent(this) {
            when (it) {
                HomeViewModel.Navigation.Add -> launchActivity<AddEditHabitActivity>()
                is HomeViewModel.Navigation.Edit ->
                    launchActivity<AddEditHabitActivity> {
                        putExtra(AddEditHabitActivity.IS_EDIT_MODE, true)
                        putExtra(AddEditHabitActivity.HABIT, it.habit)
                    }
            }
        }
        viewModel.navigateToUserDataActivity.observeEvent(this) {
            launchActivity<UserDataActivity> {
                putExtra(UserDataActivity.USER_KEY, it)
            }
        }

        viewModel.logout.observeEvent(this){
            launchActivityAndFinish<AuthenticationActivity>()
        }
    }

    private fun NavController.getHomeNavOptions(): NavOptions {
        return NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setPopUpTo(
                destinationId = currentDestination?.id ?: -1,
                inclusive = currentDestination?.id != R.id.dashboardFragment
            )
            .build()
    }

    private fun OnBackPressedCallback.onBackPressed() {
        if (isEnabled && !navController.popBackStack()) {
            isEnabled = false
            onBackPressedDispatcher.onBackPressed()
            return
        }

        binding.vwBottomNavigation.setSelectedItem(
            HomeBottomNavigationItem.getItemFromFragmentName(
                navController.currentDestination?.label?.toString() ?: ""
            )
        )
    }
}