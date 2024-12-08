package com.dalhousie.habit.viewmodel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.dalhousie.habit.data.remote.response.TodayHabitsResponse
import com.dalhousie.habit.data.repository.HabitRepository
import com.dalhousie.habit.data.repository.UserRepository
import com.dalhousie.habit.enums.HomeBottomNavigationItem
import com.dalhousie.habit.enums.HomeBottomNavigationItem.DASHBOARD
import com.dalhousie.habit.model.Habit
import com.dalhousie.habit.model.User
import com.dalhousie.habit.ui.base.BaseViewModel
import com.dalhousie.habit.util.Event
import com.dalhousie.habit.util.extensions.onError
import com.dalhousie.habit.util.extensions.onException
import com.dalhousie.habit.util.extensions.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val habitRepository: HabitRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    sealed interface Navigation {
        data object Add : Navigation
        data class Edit(val habit: Habit) : Navigation
    }

    private val _currentItemSelected = MutableLiveData<Event<HomeBottomNavigationItem>>()
    val currentItemSelected: LiveData<Event<HomeBottomNavigationItem>> get() = _currentItemSelected

    private val _todayGoals = MutableLiveData<List<TodayHabitsResponse.Data.HabitAndStatus>>()
    val todayGoals: LiveData<List<TodayHabitsResponse.Data.HabitAndStatus>> get() = _todayGoals

    private val _navigateToAddEdit = MutableLiveData<Event<Navigation>>()
    val navigateToAddEdit: LiveData<Event<Navigation>> get() = _navigateToAddEdit

    private val _loggedInUserData = MutableLiveData<User>()
    val loggedInUserData: LiveData<User> get() = _loggedInUserData

    private val _logout = MutableLiveData<Event<Unit>>()
    val logout: LiveData<Event<Unit>> = _logout

    private val _navigateToUserDataActivity = MutableLiveData<Event<User>>()
    val navigateToUserDataActivity: LiveData<Event<User>> get() = _navigateToUserDataActivity

    val todayGoalString = _todayGoals.map { goals -> goals.map { it.habit.name } }
    val isTodayGoalEmpty = todayGoalString.map { it.isEmpty() }
    val isLoadingTodayGoals = MutableLiveData(false)

    init {
        onBottomNavigationItemSelected(DASHBOARD)
        fetchTodayGoals()
        updateLoggedInUserData()
    }

    fun logout(){
        _logout.value = Event(Unit)
    }

    fun onBottomNavigationItemSelected(item: HomeBottomNavigationItem) {
        _currentItemSelected.value = Event(item)
    }

    fun navigate(navigation: Navigation) {
        _navigateToAddEdit.value = Event(navigation)
    }

    fun fetchTodayGoals() {
        viewModelScope.launch {
            isLoadingTodayGoals.postValue(true)
            habitRepository.getTodayHabits()
                .onSuccess { response -> _todayGoals.postValue(response.data.habitAndStatusList) }
                .onError { _, message -> message?.let { toast(it) } }
                .onException { throwable -> throwable.message?.let { toast(it) } }
            isLoadingTodayGoals.postValue(false)
        }
    }

    fun navigateToUserDataActivity(user: User) {
        _navigateToUserDataActivity.value = Event(user)
    }

    fun updateLoggedInUserData() {
        viewModelScope.launch {
            userRepository.getUserData()
                .onSuccess { _loggedInUserData.postValue(it.data) }
                .onError { _, message -> message?.let { toast(it) } }
                .onException { throwable -> throwable.message?.let { toast(it) } }
        }
    }
}
