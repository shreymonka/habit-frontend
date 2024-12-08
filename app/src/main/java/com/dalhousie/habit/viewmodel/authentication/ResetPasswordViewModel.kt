package com.dalhousie.habit.viewmodel.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dalhousie.habit.data.remote.request.ResetPasswordRequest
import com.dalhousie.habit.data.repository.AuthenticationRepository
import com.dalhousie.habit.ui.base.BaseViewModel
import com.dalhousie.habit.util.Event
import com.dalhousie.habit.util.extensions.onError
import com.dalhousie.habit.util.extensions.onException
import com.dalhousie.habit.util.extensions.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : BaseViewModel(){

    val newPassword = MutableLiveData("")
    val confirmNewPassword = MutableLiveData("")

    private val _newPasswordHiddenStatus = MutableLiveData(true)
    val newPasswordHiddenStatus: LiveData<Boolean> get() = _newPasswordHiddenStatus

    private val _confirmNewPasswordHiddenStatus = MutableLiveData(true)
    val confirmNewPasswordHiddenStatus: LiveData<Boolean> get() = _confirmNewPasswordHiddenStatus

    private val _onBackClick = MutableLiveData<Event<Boolean>>()
    val onBackClick: LiveData<Event<Boolean>> get() = _onBackClick

    var email = ""

    fun onBackClick() {
        _onBackClick.value = Event(true)
     }

    fun onResetPasswordClick(){
        viewModelScope.launch {
            val newPassword = newPassword.value.orEmpty()
            val confirmNewPassword = confirmNewPassword.value.orEmpty()

            if (newPassword != confirmNewPassword){
                _toastMessage.value = Event("Password Does Not Match")
                return@launch
            }

            val request = ResetPasswordRequest(email, newPassword)

            authenticationRepository.resetPassword(request)
                .onSuccess {
                    _toastMessage.value = Event("Reset Password Success")
                    onBackClick()
                }
                .onError { _, message -> message?.let { toast(it) } }
                .onException { throwable -> throwable.message?.let { toast(it) } }
        }
    }

    fun onNewPasswordEyeClick(){
        _newPasswordHiddenStatus.value?.let {
            _newPasswordHiddenStatus.value = !it
        }
    }

    fun onConfirmNewPasswordEyeClick(){
        _confirmNewPasswordHiddenStatus.value?.let {
            _confirmNewPasswordHiddenStatus.value = !it
        }
    }
}
