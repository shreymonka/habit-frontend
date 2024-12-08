package com.dalhousie.habit.ui.authentication.fragments

import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dalhousie.habit.R
import com.dalhousie.habit.databinding.FragmentSignUpBinding
import com.dalhousie.habit.ui.base.BaseFragment
import com.dalhousie.habit.util.extensions.observeEvent
import com.dalhousie.habit.util.extensions.setPasswordHiddenStatus
import com.dalhousie.habit.viewmodel.authentication.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding, SignUpViewModel>() {

    override val viewModel: SignUpViewModel by viewModels()

    override fun getLayoutResId(): Int = R.layout.fragment_sign_up

    override fun setupViewModel() {
        super.setupViewModel()
        viewModel.apply {
            passwordHiddenStatus.observe(this@SignUpFragment) {
                binding.etEnterPassword.setPasswordHiddenStatus(it)
            }
            navigateToLogin.observeEvent(this@SignUpFragment) {
                findNavController().navigate(
                    R.id.action_signUpFragment_to_loginFragment,
                    bundleOf(LoginFragment.EMAIL_KEY to it)
                )
            }
            onBackClick.observeEvent(this@SignUpFragment) {
                findNavController().popBackStack()
            }
        }
    }
}