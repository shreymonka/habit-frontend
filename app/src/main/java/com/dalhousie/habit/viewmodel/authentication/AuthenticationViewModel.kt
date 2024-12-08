package com.dalhousie.habit.viewmodel.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dalhousie.habit.ui.base.BaseViewModel
import com.dalhousie.habit.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor() : BaseViewModel() {

    private val _navigateToHomeActivity = MutableLiveData<Event<Boolean>>()
    val navigateToHomeActivity: LiveData<Event<Boolean>> get() = _navigateToHomeActivity

    fun navigateToHomeActivity() {
        _navigateToHomeActivity.value = Event(true)
    }

    companion object {
        const val MIN_PASSWORD_LENGTH = 8
    }
}
