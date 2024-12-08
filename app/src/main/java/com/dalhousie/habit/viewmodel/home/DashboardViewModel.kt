package com.dalhousie.habit.viewmodel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.dalhousie.habit.data.remote.response.HabitStatisticsResponse
import com.dalhousie.habit.data.remote.response.HabitStatisticsResponse.Data.MonthYearAndStatistics
import com.dalhousie.habit.data.repository.HabitRepository
import com.dalhousie.habit.ui.base.BaseViewModel
import com.dalhousie.habit.util.extensions.onError
import com.dalhousie.habit.util.extensions.onException
import com.dalhousie.habit.util.extensions.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val habitRepository: HabitRepository
) : BaseViewModel() {

    private val _loggedInUserName = MutableLiveData("")
    val loggedInUserName: LiveData<String> get() = _loggedInUserName

    private val _currentMonthYearAndStatistics = MutableLiveData<MonthYearAndStatistics>()
    val currentMonthYearAndStatistics: LiveData<MonthYearAndStatistics>
        get() = _currentMonthYearAndStatistics

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isHabitStatisticsEmpty = MutableLiveData(true)
    val isHabitStatisticsEmpty: LiveData<Boolean> = _isHabitStatisticsEmpty

    val currentMonthYearString = currentMonthYearAndStatistics.map {
        it.monthYear.monthYearString
    }
    val currentHabitStatisticsData = currentMonthYearAndStatistics.map {
        getHabitStatisticsData(it)
    }
    val hasStatisticsForNextMonth = currentMonthYearAndStatistics.map {
        habitStatisticsData.indexOf(it) > 0
    }
    val hasStatisticsForPreviousMonth = currentMonthYearAndStatistics.map {
        habitStatisticsData.indexOf(it) < (habitStatisticsData.count() - 1)
    }

    private var habitStatisticsData = listOf<MonthYearAndStatistics>()

    init {
        getHabitStatistics()
    }

    fun goToPreviousMonthStatistics() {
        val currentMonthYearAndStatistics = currentMonthYearAndStatistics.value ?: return
        val currentIndex = habitStatisticsData.indexOf(currentMonthYearAndStatistics)
        if (currentIndex < habitStatisticsData.count() - 1)
            _currentMonthYearAndStatistics.value = habitStatisticsData[currentIndex + 1]
    }

    fun goToNextMonthStatistics() {
        val currentMonthYearAndStatistics = currentMonthYearAndStatistics.value ?: return
        val currentIndex = habitStatisticsData.indexOf(currentMonthYearAndStatistics)
        if (currentIndex > 0)
            _currentMonthYearAndStatistics.value = habitStatisticsData[currentIndex - 1]
    }

    fun setLoggedInUserName(name: String) {
        _loggedInUserName.value = name
    }

    private fun getHabitStatistics() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            habitRepository.getHabitStatistics()
                .onSuccess { onGetHabitStatisticsSuccess(it) }
                .onError { _, message -> message?.let { toast(it) } }
                .onException { throwable -> throwable.message?.let { toast(it) } }
            _isLoading.postValue(false)
        }
    }

    private fun onGetHabitStatisticsSuccess(response: HabitStatisticsResponse) {
        habitStatisticsData = response.data.data.reversed()
        _isHabitStatisticsEmpty.value = habitStatisticsData.isEmpty()
        if (habitStatisticsData.isNotEmpty())
            _currentMonthYearAndStatistics.postValue(habitStatisticsData.first())
    }

    private fun getHabitStatisticsData(data: MonthYearAndStatistics): List<Float> =
        IntRange(1, data.monthYear.numberOfDaysInMonth).map { dayOfMonth ->
            val date = data.monthYear.getDateForDayOfMonth(dayOfMonth)
            data.habitStatistics
                .filter { it.habit.schedule.contains(date.dayOfWeek.name) }
                .let { totalHabits ->
                    val totalHabitsCount = totalHabits.count()
                    if (totalHabitsCount == 0)
                        return@let 0f
                    val completedHabitCount =
                        totalHabits.count { it.completionLocalDates.contains(date) }
                    (completedHabitCount.toFloat() / totalHabitsCount)
                }
        }
}