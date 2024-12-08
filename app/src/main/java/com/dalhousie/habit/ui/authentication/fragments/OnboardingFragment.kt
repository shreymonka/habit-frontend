package com.dalhousie.habit.ui.authentication.fragments

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dalhousie.habit.R
import com.dalhousie.habit.databinding.FragmentOnboardingBinding
import com.dalhousie.habit.ui.base.BaseFragment
import com.dalhousie.habit.util.extensions.observeEvent
import com.dalhousie.habit.viewmodel.authentication.AuthenticationViewModel
import com.dalhousie.habit.viewmodel.authentication.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingFragment : BaseFragment<FragmentOnboardingBinding, OnboardingViewModel>() {

    override val viewModel: OnboardingViewModel by viewModels()
    private val authenticationViewModel: AuthenticationViewModel by activityViewModels()

    override fun getLayoutResId(): Int = R.layout.fragment_onboarding

    override fun setupViewModel() {
        viewModel.navigationEvent.observeEvent(this) {
            when (it) {
                OnboardingViewModel.Navigation.Login ->
                    findNavController().navigate(R.id.action_onboardingFragment_to_loginFragment)
                OnboardingViewModel.Navigation.SignUp ->
                    findNavController().navigate(R.id.action_onboardingFragment_to_signUpFragment)
                OnboardingViewModel.Navigation.Home ->
                    authenticationViewModel.navigateToHomeActivity()
            }
        }
    }
}