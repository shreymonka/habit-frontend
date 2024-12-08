package com.dalhousie.habit.ui.authentication

import androidx.activity.viewModels
import com.dalhousie.habit.R
import com.dalhousie.habit.databinding.ActivityAuthenticationBinding
import com.dalhousie.habit.ui.base.BaseAppCompatActivity
import com.dalhousie.habit.ui.home.HomeActivity
import com.dalhousie.habit.util.extensions.launchActivityAndFinish
import com.dalhousie.habit.util.extensions.observeEvent
import com.dalhousie.habit.viewmodel.authentication.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthenticationActivity :
    BaseAppCompatActivity<ActivityAuthenticationBinding, AuthenticationViewModel>() {

    override val viewModel: AuthenticationViewModel by viewModels()

    override fun getLayoutResId(): Int = R.layout.activity_authentication

    override fun setupViewModel() {
        viewModel.navigateToHomeActivity.observeEvent(this) {
            launchActivityAndFinish<HomeActivity>()
        }
    }
}