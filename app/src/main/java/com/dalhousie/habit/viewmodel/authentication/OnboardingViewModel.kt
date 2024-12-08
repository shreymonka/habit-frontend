package com.dalhousie.habit.viewmodel.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dalhousie.habit.ui.base.BaseViewModel
import com.dalhousie.habit.util.Event
import com.dalhousie.habit.util.components.AppSharedPreferences
import com.dalhousie.habit.util.components.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val networkHelper: NetworkHelper,
    private val appSharedPreferences: AppSharedPreferences
) : BaseViewModel() {

    sealed interface Navigation {
        data object Login : Navigation
        data object SignUp : Navigation
        data object Home : Navigation
    }

    private val _navigationEvent = MutableLiveData<Event<Navigation>>()
    val navigationEvent: LiveData<Event<Navigation>> get() = _navigationEvent

    private val _isInternetAvailable = MutableLiveData<Boolean>()
    val isInternetAvailable: LiveData<Boolean> get() = _isInternetAvailable

    init {
        checkInternetAndLoggedInStatus()
    }

    fun onLoginClicked() {
        _navigationEvent.value = Event(Navigation.Login)
    }

    fun onSignUpClicked() {
        _navigationEvent.value = Event(Navigation.SignUp)
    }

    private fun checkInternetAndLoggedInStatus() {
        val isNetworkAvailable = networkHelper.isNetworkAvailable()
        _isInternetAvailable.value = isNetworkAvailable
        if (isNetworkAvailable && appSharedPreferences.isUserLoggedIn())
            _navigationEvent.value = Event(Navigation.Home)
    }
}