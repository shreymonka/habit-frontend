package com.dalhousie.habit.ui.authentication.fragments

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dalhousie.habit.R
import com.dalhousie.habit.databinding.FragmentLoginBinding
import com.dalhousie.habit.ui.base.BaseFragment
import com.dalhousie.habit.util.extensions.observeEvent
import com.dalhousie.habit.util.extensions.setPasswordHiddenStatus
import com.dalhousie.habit.viewmodel.authentication.AuthenticationViewModel
import com.dalhousie.habit.viewmodel.authentication.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {

    override val viewModel: LoginViewModel by viewModels()
    private val authenticationViewModel: AuthenticationViewModel by activityViewModels()

    override fun getLayoutResId(): Int = R.layout.fragment_login

    override fun setupViewModel() {
        super.setupViewModel()
        viewModel.apply {
            passwordHiddenStatus.observe(this@LoginFragment) {
                binding.etEnterPassword.setPasswordHiddenStatus(it)
            }
            navigateToHome.observeEvent(this@LoginFragment) {
                authenticationViewModel.navigateToHomeActivity()
            }
            onBackClick.observeEvent(this@LoginFragment) {
                findNavController().popBackStack()
            }
            navigateToForgotPassword.observeEvent(this@LoginFragment) {
                findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
            }
            rememberMeStatus.observe(this@LoginFragment) {
                binding.ivRememberMe.isSelected = it
            }
        }
    }

    companion object {
        const val EMAIL_KEY = "email_key"
    }
}
