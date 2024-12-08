package com.dalhousie.habit.viewmodel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.dalhousie.habit.data.repository.UserRepository
import com.dalhousie.habit.model.User
import com.dalhousie.habit.ui.base.BaseViewModel
import com.dalhousie.habit.util.Event
import com.dalhousie.habit.util.extensions.onError
import com.dalhousie.habit.util.extensions.onException
import com.dalhousie.habit.util.extensions.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {

    val query = MutableLiveData("")

    private val _userList = MutableLiveData<List<User>>()
    val userList: LiveData<List<User>> get() = _userList

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _navigateToUserData = MutableLiveData<Event<User>>()
    val navigateToUserData: LiveData<Event<User>> get() = _navigateToUserData

    val shouldShowRecyclerView: LiveData<Boolean> = _userList.map { it.isNotEmpty() }

    private var searchJob: Job? = null

    fun navigateToUserData(user: User) {
        _navigateToUserData.value = Event(user)
    }

    fun searchUsers(query: String) {
        searchJob?.cancel()

        if (query.isEmpty()) {
            _userList.postValue(emptyList())
            return
        }

        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_TIME)
            _isLoading.postValue(true)
            userRepository.searchUsers(query)
                .onSuccess { _userList.postValue(it.data.users) }
                .onError { _, message ->
                    message?.let { toast(it) }
                    _userList.postValue(emptyList())
                }
                .onException { throwable ->
                    throwable.message?.let { toast(it) }
                    _userList.postValue(emptyList())
                }
            _isLoading.postValue(false)
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_TIME = 1000L
    }
}
