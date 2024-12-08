package com.dalhousie.habit.viewmodel.authentication

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dalhousie.habit.R
import com.dalhousie.habit.data.remote.request.ForgotPasswordRequest
import com.dalhousie.habit.data.remote.request.OtpVerificationRequest
import com.dalhousie.habit.data.repository.AuthenticationRepository
import com.dalhousie.habit.ui.base.BaseViewModel
import com.dalhousie.habit.util.Event
import com.dalhousie.habit.util.components.ResourceHelper
import com.dalhousie.habit.util.extensions.onError
import com.dalhousie.habit.util.extensions.onException
import com.dalhousie.habit.util.extensions.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val resourceHelper: ResourceHelper,
    private val authenticationRepository: AuthenticationRepository
) : BaseViewModel() {

    val email = MutableLiveData("")
    val otp = MutableLiveData("")
    val isOtpSent = MutableLiveData(false)
    val sendOtpText = MutableLiveData(resourceHelper.getString(R.string.send_otp))

    // Navigation events
    private val _navigateToResetPassword = MutableLiveData<Event<String>>()
    val navigateToResetPassword: LiveData<Event<String>> get() = _navigateToResetPassword

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _onBackClick = MutableLiveData<Event<Boolean>>()
    val onBackClick: LiveData<Event<Boolean>> get() = _onBackClick

    fun onBackClick() {
        _onBackClick.value = Event(true)
    }

    fun onSendOtpClick() {
        val email = email.value ?: return
        if (!validateEmail(email)) return

        viewModelScope.launch {
            _isLoading.postValue(true)
            authenticationRepository.forgotPassword(ForgotPasswordRequest(email))
                .onSuccess {
                    isOtpSent.postValue(true)
                    sendOtpText.postValue(resourceHelper.getString(R.string.otp_sent))
                }
                .onError { _, message -> toast(message ?: "Error occurred") }
                .onException { throwable -> toast(throwable.message ?: "Exception occurred") }
            _isLoading.postValue(false)
        }
    }

    fun onResetPasswordClick() {
        val otp = otp.value ?: return
        val email = email.value ?: return
        if (!validateOtp(otp)) return

        viewModelScope.launch {
            _isLoading.postValue(true)
            authenticationRepository.otpVerification(OtpVerificationRequest(otp, email))
                .onSuccess {
                    _navigateToResetPassword.postValue(Event(email))
                }
                .onError { _, message -> toast(message ?: "Error occurred") }
                .onException { throwable -> toast(throwable.message ?: "Exception occurred") }
            _isLoading.postValue(false)
        }
    }

    private fun validateEmail(email: String): Boolean = when {
        email.isBlank() -> {
            toast(resourceHelper.getString(R.string.txt_empty_email_field))
            false
        }

        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
            toast(resourceHelper.getString(R.string.txt_invalid_email))
            false
        }

        else -> true
    }

    private fun validateOtp(otp: String): Boolean = when {
        otp.isBlank() -> {
            toast(resourceHelper.getString(R.string.txt_empty_otp_field))
            false
        }

        else -> true
    }
}
