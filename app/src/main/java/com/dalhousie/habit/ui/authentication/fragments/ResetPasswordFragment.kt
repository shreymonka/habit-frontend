package com.dalhousie.habit.ui.authentication.fragments

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dalhousie.habit.R
import com.dalhousie.habit.databinding.FragmentResetPasswordBinding
import com.dalhousie.habit.ui.base.BaseFragment
import com.dalhousie.habit.util.extensions.observeEvent
import com.dalhousie.habit.util.extensions.setPasswordHiddenStatus
import com.dalhousie.habit.viewmodel.authentication.ResetPasswordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPasswordFragment : BaseFragment<FragmentResetPasswordBinding, ResetPasswordViewModel>() {

    override val viewModel: ResetPasswordViewModel by viewModels()

    override fun getLayoutResId(): Int = R.layout.fragment_reset_password

    override fun initialize() {
        arguments?.let {
            it.getString(EMAIL)?.let  { email ->
                viewModel.email = email
            }
        }
    }

    override fun setupViewModel() {
        super.setupViewModel()
        viewModel.onBackClick.observeEvent(this){
            findNavController().popBackStack()
        }

        viewModel.newPasswordHiddenStatus.observe(this){
            binding.etEnterPassword.setPasswordHiddenStatus(it)
        }

        viewModel.confirmNewPasswordHiddenStatus.observe(this){
            binding.etReEnterPassword.setPasswordHiddenStatus(it)
        }

    }

    companion object {
        const val EMAIL = "EMAIL"
    }

}