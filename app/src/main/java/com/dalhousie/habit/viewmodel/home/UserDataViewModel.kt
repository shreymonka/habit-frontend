package com.dalhousie.habit.viewmodel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dalhousie.habit.R
import com.dalhousie.habit.data.remote.request.AddEditHabitRequest
import com.dalhousie.habit.data.repository.HabitRepository
import com.dalhousie.habit.model.Habit
import com.dalhousie.habit.model.User
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
class UserDataViewModel @Inject constructor(
    private val habitRepository: HabitRepository,
    private val resourceHelper: ResourceHelper
) : BaseViewModel() {

    private val _onBackClick = MutableLiveData<Event<Boolean>>()
    val onBackClick: LiveData<Event<Boolean>> get() = _onBackClick

    private val _userData = MutableLiveData<User>()
    val userData: LiveData<User> get() = _userData

    private val _showHabitDialog = MutableLiveData<Event<Habit>>()
    val showHabitDialog: LiveData<Event<Habit>> get() = _showHabitDialog

    fun onBackClick() {
        _onBackClick.value = Event(true)
    }

    fun setUserData(user: User) {
        _userData.value = user
    }

    fun showHabitDetailsDialog(habit: Habit) {
        _showHabitDialog.value = Event(habit)
    }

    fun copyHabit(habit: Habit) {
        viewModelScope.launch {
            val request = AddEditHabitRequest(name = habit.name, schedule = habit.schedule)
            habitRepository.addHabit(request)
                .onSuccess { toast(resourceHelper.getString(R.string.txt_copied)) }
                .onError { _, message -> message?.let { toast(it) } }
                .onException { throwable -> throwable.message?.let { toast(it) } }
        }
    }
}