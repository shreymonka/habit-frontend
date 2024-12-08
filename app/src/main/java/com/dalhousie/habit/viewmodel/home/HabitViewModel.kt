package com.dalhousie.habit.viewmodel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dalhousie.habit.R
import com.dalhousie.habit.data.remote.request.MarkHabitAsCompleteRequest
import com.dalhousie.habit.data.remote.response.TodayHabitsResponse
import com.dalhousie.habit.data.repository.HabitRepository
import com.dalhousie.habit.model.Habit
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
class HabitViewModel @Inject constructor(
    private val habitRepository: HabitRepository,
    private val resourceHelper: ResourceHelper
) : BaseViewModel() {

    private val _habits = MutableLiveData<List<Habit>>()
    val habits: LiveData<List<Habit>> = _habits

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _navigateToAddEdit = MutableLiveData<Event<HomeViewModel.Navigation>>()
    val navigateToAddEdit: LiveData<Event<HomeViewModel.Navigation>> = _navigateToAddEdit

    private val _fetchTodaysGoal = MutableLiveData<Event<Boolean>>()
    val fetchTodaysGoal: LiveData<Event<Boolean>> get() = _fetchTodaysGoal

    private val _showMarkHabitCompleteDialog = MutableLiveData<Event<String>>()
    val showMarkHabitCompleteDialog: LiveData<Event<String>> get() = _showMarkHabitCompleteDialog

    val isTodayGoalEmpty = MutableLiveData(true)
    val isHabitsEmpty = MutableLiveData(true)

    fun onAddHabitClick() {
        _navigateToAddEdit.value = Event(HomeViewModel.Navigation.Add)
    }

    fun onEditHabitClick(habit: Habit) {
        _navigateToAddEdit.value = Event(HomeViewModel.Navigation.Edit(habit))
    }

    fun onMarkHabitCompleteClicked(habitAndStatus: TodayHabitsResponse.Data.HabitAndStatus) {
        if (habitAndStatus.isCompleted) {
            toast(resourceHelper.getString(R.string.txt_cannot_undo_completed_habit))
            return
        }
        _showMarkHabitCompleteDialog.value = Event(habitAndStatus.habit.id)
    }

    fun markHabitAsComplete(habitId: String) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            habitRepository.markHabitAsComplete(MarkHabitAsCompleteRequest(habitId))
                .onSuccess { _fetchTodaysGoal.value = Event(true) }
                .onError { _, message -> message?.let { toast(it) } }
                .onException { throwable -> throwable.message?.let { toast(it) } }
            _isLoading.postValue(false)
        }
    }

    fun fetchHabits() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            habitRepository.getHabits()
                .onSuccess { response -> _habits.value = response.data.habits }
                .onError { _, message -> message?.let { toast(it) } }
                .onException { throwable -> throwable.message?.let { toast(it) } }
            _isLoading.postValue(false)
        }
    }
}