package com.dalhousie.habit.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dalhousie.habit.util.Event

/**
 * Base view model for all [ViewModel]s.
 */
abstract class BaseViewModel : ViewModel() {

    protected val _toastMessage = MutableLiveData<Event<String>>()
    val toastMessage: LiveData<Event<String>> get() = _toastMessage

    protected fun toast(message: String) {
        _toastMessage.value = Event(message)
    }
}