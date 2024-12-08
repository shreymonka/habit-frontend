package com.dalhousie.habit.ui.home.fragments

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.dalhousie.habit.R
import com.dalhousie.habit.adapter.UserImageAdapter
import com.dalhousie.habit.databinding.FragmentSettingsBinding
import com.dalhousie.habit.databinding.LayoutChangePasswordDialogBinding
import com.dalhousie.habit.databinding.LayoutEditImageDialogBinding
import com.dalhousie.habit.databinding.LayoutLogoutDialogBinding
import com.dalhousie.habit.ui.base.BaseFragment
import com.dalhousie.habit.util.extensions.observeEvent
import com.dalhousie.habit.util.extensions.setPasswordHiddenStatus
import com.dalhousie.habit.viewmodel.home.HomeViewModel
import com.dalhousie.habit.viewmodel.home.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding, SettingsViewModel>() {

    override val viewModel: SettingsViewModel by viewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()

    private val userImageAdapter by lazy {
        UserImageAdapter(viewModel::onNewUserImageClicked)
    }

    override fun getLayoutResId(): Int = R.layout.fragment_settings

    override fun setupViewModel() {
        super.setupViewModel()
        homeViewModel.loggedInUserData.observe(this) {
            viewModel.setLoggedInUserData(it)
        }
        viewModel.navigateToLogin.observeEvent(this) {
            homeViewModel.logout()
        }
        viewModel.showDialog.observeEvent(this) {
            when (it) {
                SettingsViewModel.Dialog.ChangePassword ->
                    showChangePasswordDialog(viewModel::onUpdatePasswordClicked)

                SettingsViewModel.Dialog.EditProfileImage ->
                    showEditProfileImageDialog()

                SettingsViewModel.Dialog.Logout ->
                    showConfirmLogoutDialog(viewModel::setUserLoggedOut)
            }
        }
        viewModel.updateLoggedInUserData.observeEvent(this) {
            homeViewModel.updateLoggedInUserData()
        }
        viewModel.userImageList.observe(this) {
            userImageAdapter.setItems(it)
        }
    }

    private fun showEditProfileImageDialog() {
        val dialog = AlertDialog.Builder(requireContext()).create()
        LayoutEditImageDialogBinding.inflate(layoutInflater).apply {
            rvImages.adapter = userImageAdapter
            btnUpdate.setOnClickListener {
                userImageAdapter.selectedUserImage?.id?.let { profilePicId ->
                    viewModel.updateProfileImage(profilePicId)
                }
                dialog.dismiss()
            }
            btnCancel.setOnClickListener {
                viewModel.resetUserImageList()
                dialog.dismiss()
            }
            dialog.setView(root)
            dialog.show()
        }
    }

    private fun showChangePasswordDialog(
        onUpdatePasswordClicked: (String, String, String, (Boolean) -> Unit) -> Unit
    ) {
        val dialog = AlertDialog.Builder(requireContext()).create()
        LayoutChangePasswordDialogBinding.inflate(layoutInflater).apply {
            ivOldPasswordEye.setOnClickListener {
                etOldPassword.setPasswordHiddenStatus(etOldPassword.transformationMethod == null)
            }
            ivNewPasswordEye.setOnClickListener {
                etNewPassword.setPasswordHiddenStatus(etNewPassword.transformationMethod == null)
            }
            ivRePasswordEye.setOnClickListener {
                etReEnterPassword.setPasswordHiddenStatus(etReEnterPassword.transformationMethod == null)
            }
            btnUpdate.setOnClickListener {
                val oldPassword = etOldPassword.text.toString()
                val newPassword = etNewPassword.text.toString()
                val reEnterPassword = etReEnterPassword.text.toString()
                onUpdatePasswordClicked(oldPassword, newPassword, reEnterPassword) {
                    if (it) dialog.dismiss()
                }
            }
            btnCancel.setOnClickListener { dialog.dismiss() }
            dialog.setView(root)
            dialog.show()
        }
    }

    private fun showConfirmLogoutDialog(onLogout: () -> Unit) {
        val dialog = AlertDialog.Builder(requireContext()).create()
        LayoutLogoutDialogBinding.inflate(layoutInflater).apply {
            btnYes.setOnClickListener { onLogout() }
            btnCancel.setOnClickListener { dialog.dismiss() }
            dialog.setView(root)
            dialog.show()
        }
    }
}