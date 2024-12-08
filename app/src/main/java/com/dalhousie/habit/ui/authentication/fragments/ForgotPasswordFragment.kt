package com.dalhousie.habit.ui.authentication.fragments

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dalhousie.habit.R
import com.dalhousie.habit.databinding.FragmentForgotPasswordBinding
import com.dalhousie.habit.ui.base.BaseFragment
import com.dalhousie.habit.util.extensions.observeEvent
import com.dalhousie.habit.viewmodel.authentication.ForgotPasswordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordFragment :
    BaseFragment<FragmentForgotPasswordBinding, ForgotPasswordViewModel>() {

    override val viewModel: ForgotPasswordViewModel by viewModels()

    override fun getLayoutResId(): Int = R.layout.fragment_forgot_password

    override fun setupViewModel() {
        super.setupViewModel()
        viewModel.apply {
            navigateToResetPassword.observeEvent(this@ForgotPasswordFragment) { email ->
                val bundle = Bundle().apply {
                    putString(ResetPasswordFragment.EMAIL, email)
                }
                findNavController().navigate(
                    R.id.action_forgotPasswordFragment_to_resetPasswordFragment,
                    bundle
                )
            }
            onBackClick.observeEvent(this@ForgotPasswordFragment) {
                findNavController().popBackStack()
            }
        }
    }
}
