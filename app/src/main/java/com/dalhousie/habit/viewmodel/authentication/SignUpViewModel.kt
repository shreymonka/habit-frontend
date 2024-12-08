package com.dalhousie.habit.viewmodel.authentication

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.dalhousie.habit.R
import com.dalhousie.habit.data.remote.request.SignUpRequest
import com.dalhousie.habit.data.repository.AuthenticationRepository
import com.dalhousie.habit.ui.base.BaseViewModel
import com.dalhousie.habit.util.Event
import com.dalhousie.habit.util.components.ResourceHelper
import com.dalhousie.habit.util.extensions.onError
import com.dalhousie.habit.util.extensions.onException
import com.dalhousie.habit.util.extensions.onSuccess
import com.dalhousie.habit.viewmodel.authentication.AuthenticationViewModel.Companion.MIN_PASSWORD_LENGTH
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val resourceHelper: ResourceHelper,
    private val authenticationRepository: AuthenticationRepository
) : BaseViewModel() {

    val userName = MutableLiveData("")
    val email = MutableLiveData("")
    val password = MutableLiveData("")

    private val _passwordHiddenStatus = MutableLiveData(true)
    val passwordHiddenStatus: LiveData<Boolean> get() = _passwordHiddenStatus

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _navigateToLogin = MutableLiveData<Event<String>>()
    val navigateToLogin: LiveData<Event<String>> get() = _navigateToLogin

    private val _onBackClick = MutableLiveData<Event<Boolean>>()
    val onBackClick: LiveData<Event<Boolean>> get() = _onBackClick

    val signupButtonText = isLoading.map { loading ->
        if (loading) "" else resourceHelper.getString(R.string.txt_sign_up)
    }

    fun onBackClick() {
        _onBackClick.value = Event(true)
    }

    fun onPasswordEyeClicked() {
        _passwordHiddenStatus.value = !(passwordHiddenStatus.value ?: false)
    }

    fun onSignUpClicked() {
        val userName = userName.value ?: return
        val email = email.value ?: return
        val password = password.value ?: return
        if (!validateInput(userName, email, password))
            return
        registerUser(SignUpRequest(userName, email, password))
    }

    private fun validateInput(userName: String, email: String, password: String): Boolean = when {
        userName.isBlank() || email.isBlank() || password.isBlank() -> {
            toast(resourceHelper.getString(R.string.txt_empty_input_field))
            false
        }

        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
            toast(resourceHelper.getString(R.string.txt_invalid_email))
            false
        }

        password.length < MIN_PASSWORD_LENGTH -> {
            toast(resourceHelper.getString(R.string.txt_invalid_password))
            false
        }

        else -> true
    }

    private fun registerUser(request: SignUpRequest) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            authenticationRepository.registerUser(request)
                .onSuccess { onRegisterSuccess(request.email) }
                .onError { _, message -> message?.let { toast(it) } }
                .onException { throwable -> throwable.message?.let { toast(it) } }
            _isLoading.postValue(false)
        }
    }

    private fun onRegisterSuccess(email: String) {
        _navigateToLogin.value = Event(email)
    }
}