package com.dalhousie.habit.viewmodel.authentication

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.dalhousie.habit.R
import com.dalhousie.habit.data.remote.request.LoginRequest
import com.dalhousie.habit.data.remote.response.LoginResponse
import com.dalhousie.habit.data.repository.AuthenticationRepository
import com.dalhousie.habit.ui.base.BaseViewModel
import com.dalhousie.habit.util.Event
import com.dalhousie.habit.util.components.AppSharedPreferences
import com.dalhousie.habit.util.components.ResourceHelper
import com.dalhousie.habit.util.extensions.onError
import com.dalhousie.habit.util.extensions.onException
import com.dalhousie.habit.util.extensions.onSuccess
import com.dalhousie.habit.viewmodel.authentication.AuthenticationViewModel.Companion.MIN_PASSWORD_LENGTH
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val resourceHelper: ResourceHelper,
    private val appSharedPreferences: AppSharedPreferences,
    private val authenticationRepository: AuthenticationRepository
) : BaseViewModel() {

    val email = MutableLiveData(appSharedPreferences.getRememberedEmail() ?: "")
    val password = MutableLiveData("")

    private val _passwordHiddenStatus = MutableLiveData(true)
    val passwordHiddenStatus: LiveData<Boolean> get() = _passwordHiddenStatus

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _navigateToHome = MutableLiveData<Event<Boolean>>()
    val navigateToHome: LiveData<Event<Boolean>> get() = _navigateToHome

    private val _navigateToForgotPassword = MutableLiveData<Event<Unit>>()
    val navigateToForgotPassword: LiveData<Event<Unit>> get() = _navigateToForgotPassword

    private val _onBackClick = MutableLiveData<Event<Boolean>>()
    val onBackClick: LiveData<Event<Boolean>> get() = _onBackClick

    private val _rememberMeStatus = MutableLiveData(false)
    val rememberMeStatus: LiveData<Boolean> get() = _rememberMeStatus

    val loginButtonText = isLoading.map { loading ->
        if (loading) "" else resourceHelper.getString(R.string.txt_login)
    }

    fun onBackClick() {
        _onBackClick.value = Event(true)
    }

    fun onPasswordEyeClicked() {
        _passwordHiddenStatus.value = !(passwordHiddenStatus.value ?: false)
    }

    fun onForgotPasswordClick() {
        _navigateToForgotPassword.value = Event(Unit)
    }

    fun onRememberMeClicked() {
        _rememberMeStatus.value = !(rememberMeStatus.value ?: false)
    }

    fun onLoginClicked() {
        val email = email.value ?: return
        val password = password.value ?: return
        if (!validateInput(email, password))
            return
        loginUser(LoginRequest(email, password))
    }

    private fun validateInput(email: String, password: String): Boolean = when {
        email.isBlank() || password.isBlank() -> {
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

    private fun loginUser(request: LoginRequest) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            authenticationRepository.loginUser(request)
                .onSuccess { onLoginSuccess(request.email, it) }
                .onError { _, message -> message?.let { toast(it) } }
                .onException { throwable -> throwable.message?.let { toast(it) } }
            _isLoading.postValue(false)
        }
    }

    private fun onLoginSuccess(email: String, response: LoginResponse) {
        appSharedPreferences.apply {
            setUserLoggedIn(true)
            setAuthToken(response.data.token)
            setRememberedEmail(if(rememberMeStatus.value == true) email else "")
        }
        _navigateToHome.value = Event(true)
    }
}
