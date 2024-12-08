package com.dalhousie.habit.viewmodel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dalhousie.habit.R
import com.dalhousie.habit.data.remote.request.AddEditHabitRequest
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
class AddEditHabitViewModel @Inject constructor(
    private val habitRepository: HabitRepository,
    private val resourceHelper: ResourceHelper
) : BaseViewModel() {

    sealed interface Navigation {
        data object Back : Navigation
    }

    private val _navigationEvent = MutableLiveData<Event<Navigation>>()
    val navigationEvent: LiveData<Event<Navigation>> get() = _navigationEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    val habitName = MutableLiveData("")

    // Selected days state
    val mondaySelected = MutableLiveData(false)
    val tuesdaySelected = MutableLiveData(false)
    val wednesdaySelected = MutableLiveData(false)
    val thursdaySelected = MutableLiveData(false)
    val fridaySelected = MutableLiveData(false)
    val saturdaySelected = MutableLiveData(false)
    val sundaySelected = MutableLiveData(false)

    private val _isEditMode = MutableLiveData(false)
    val isEditMode: LiveData<Boolean> get() = _isEditMode

    private var habit: Habit? = null

    fun onBackClick() {
        _navigationEvent.value = Event(Navigation.Back)
    }

    fun onDayClick(dayIndex: Int) {
        when (dayIndex) {
            0 -> mondaySelected.value = !(mondaySelected.value ?: false)
            1 -> tuesdaySelected.value = !(tuesdaySelected.value ?: false)
            2 -> wednesdaySelected.value = !(wednesdaySelected.value ?: false)
            3 -> thursdaySelected.value = !(thursdaySelected.value ?: false)
            4 -> fridaySelected.value = !(fridaySelected.value ?: false)
            5 -> saturdaySelected.value = !(saturdaySelected.value ?: false)
            6 -> sundaySelected.value = !(sundaySelected.value ?: false)
        }
    }

    fun onActionButtonClick() {
        val name = habitName.value
        if (name.isNullOrBlank()) {
            toast("Please enter habit name")
            return
        }

        if (!isAnyDaySelected()) {
            toast("Please select at least one day")
            return
        }

        val selectedDays = getSelectedDays()
        val request = AddEditHabitRequest(habit?.id, name, selectedDays)

        viewModelScope.launch {
            if (isEditMode.value == true) {
                editHabit(request)
            } else {
                addHabit(request)
            }
        }
    }

    fun onDeleteClicked() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val habitId = habit?.id ?: return@launch
            habitRepository.deleteHabit(habitId)
                .onSuccess {
                    toast(resourceHelper.getString(R.string.delete_habit_success))
                    _navigationEvent.postValue(Event(Navigation.Back))
                }
                .onError { _, message -> message?.let { toast(it) } }
                .onException { throwable -> throwable.message?.let { toast(it) } }
            _isLoading.postValue(false)
        }
    }

    private suspend fun addHabit(request: AddEditHabitRequest) {
        _isLoading.postValue(true)
        habitRepository.addHabit(request)
            .onSuccess {
                toast(resourceHelper.getString(R.string.add_habit_success))
                _navigationEvent.postValue(Event(Navigation.Back))
            }
            .onError { _, message -> message?.let { toast(it) } }
            .onException { throwable -> throwable.message?.let { toast(it) } }
        _isLoading.postValue(false)
    }

    private suspend fun editHabit(request: AddEditHabitRequest) {
        _isLoading.postValue(true)
        habitRepository.editHabit(request)
            .onSuccess {
                toast(resourceHelper.getString(R.string.edit_habit_success))
                _navigationEvent.postValue(Event(Navigation.Back))
            }
            .onError { _, message -> message?.let { toast(it) } }
            .onException { throwable -> throwable.message?.let { toast(it) } }
        _isLoading.postValue(false)
    }

    private fun isAnyDaySelected(): Boolean {
        return mondaySelected.value == true ||
                tuesdaySelected.value == true ||
                wednesdaySelected.value == true ||
                thursdaySelected.value == true ||
                fridaySelected.value == true ||
                saturdaySelected.value == true ||
                sundaySelected.value == true
    }

    private fun getSelectedDays(): List<String> {
        return mutableListOf<String>().apply {
            if (mondaySelected.value == true) add("MONDAY")
            if (tuesdaySelected.value == true) add("TUESDAY")
            if (wednesdaySelected.value == true) add("WEDNESDAY")
            if (thursdaySelected.value == true) add("THURSDAY")
            if (fridaySelected.value == true) add("FRIDAY")
            if (saturdaySelected.value == true) add("SATURDAY")
            if (sundaySelected.value == true) add("SUNDAY")
        }
    }

    fun setEditMode(isEdit: Boolean) {
        _isEditMode.value = isEdit
        if (isEdit) {
            habit?.apply {
                habitName.value = name
                schedule.forEach { day ->
                    when (day) {
                        "MONDAY" -> mondaySelected.value = true
                        "TUESDAY" -> tuesdaySelected.value = true
                        "WEDNESDAY" -> wednesdaySelected.value = true
                        "THURSDAY" -> thursdaySelected.value = true
                        "FRIDAY" -> fridaySelected.value = true
                        "SATURDAY" -> saturdaySelected.value = true
                        "SUNDAY" -> sundaySelected.value = true
                    }
                }
            }
        }
    }

    fun setHabit(habit: Habit) {
        this.habit = habit
    }
}