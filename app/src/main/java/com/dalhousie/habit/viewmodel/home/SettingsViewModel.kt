package com.dalhousie.habit.viewmodel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.dalhousie.habit.R
import com.dalhousie.habit.data.remote.request.UpdatePasswordRequest
import com.dalhousie.habit.data.remote.request.UpdateProfilePicRequest
import com.dalhousie.habit.data.remote.request.UpdateUsernameRequest
import com.dalhousie.habit.data.repository.UserRepository
import com.dalhousie.habit.model.User
import com.dalhousie.habit.model.UserImage
import com.dalhousie.habit.ui.base.BaseViewModel
import com.dalhousie.habit.util.Event
import com.dalhousie.habit.util.components.AppSharedPreferences
import com.dalhousie.habit.util.components.ResourceHelper
import com.dalhousie.habit.util.extensions.onError
import com.dalhousie.habit.util.extensions.onException
import com.dalhousie.habit.util.extensions.onSuccess
import com.dalhousie.habit.viewmodel.authentication.AuthenticationViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val appSharedPreferences: AppSharedPreferences,
    private val resourceHelper: ResourceHelper,
    private val userRepository: UserRepository
) : BaseViewModel() {

    sealed interface Dialog {
        data object EditProfileImage : Dialog
        data object ChangePassword : Dialog
        data object Logout : Dialog
    }

    val userName = MutableLiveData("")

    private val _showDialog = MutableLiveData<Event<Dialog>>()
    val showDialog: LiveData<Event<Dialog>> get() = _showDialog

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _navigateToLogin = MutableLiveData<Event<Unit>>()
    val navigateToLogin: LiveData<Event<Unit>> = _navigateToLogin

    private val _loggedInUserData = MutableLiveData<User>()
    val loggedInUserData: LiveData<User> get() = _loggedInUserData

    private val _updateLoggedInUserData = MutableLiveData<Event<Boolean>>()
    val updateLoggedInUserData: LiveData<Event<Boolean>> get() = _updateLoggedInUserData

    private val _userImageList = MutableLiveData<List<UserImage>>()
    val userImageList: LiveData<List<UserImage>> get() = _userImageList

    val hasUsernameChanged = userName.map {
        it != _loggedInUserData.value?.userName
    }

    fun setLoggedInUserData(user: User) {
        _loggedInUserData.value = user
        userName.value = user.userName
        _userImageList.value = UserImage.getUserImageList(user.profilePicId)
    }

    fun onEditImageClicked() {
        _showDialog.value = Event(Dialog.EditProfileImage)
    }

    fun onChangePasswordClicked() {
        _showDialog.value = Event(Dialog.ChangePassword)
    }

    fun logoutClicked() {
        _showDialog.value = Event(Dialog.Logout)
    }

    fun setUserLoggedOut() {
        appSharedPreferences.setAuthToken("")
        appSharedPreferences.setUserLoggedIn(false)
        _navigateToLogin.value = Event(Unit)
    }

    fun onSaveUsernameClicked() {
        viewModelScope.launch {
            val username = userName.value ?: return@launch
            _isLoading.postValue(true)
            userRepository.updateUsername(UpdateUsernameRequest(username))
                .onSuccess {
                    _updateLoggedInUserData.postValue(Event(true))
                    toast(it.message)
                }
                .onError { _, message -> message?.let { toast(it) } }
                .onException { throwable -> throwable.message?.let { toast(it) } }
            _isLoading.postValue(false)
        }
    }

    fun onUpdatePasswordClicked(
        oldPassword: String,
        newPassword: String,
        reEnterPassword: String,
        dismissDialog: (Boolean) -> Unit
    ) {
        when {
            oldPassword.length < AuthenticationViewModel.MIN_PASSWORD_LENGTH ||
                    newPassword.length < AuthenticationViewModel.MIN_PASSWORD_LENGTH ->
                toast(resourceHelper.getString(R.string.txt_invalid_password))

            reEnterPassword != newPassword ->
                toast(resourceHelper.getString(R.string.txt_password_not_matching))

            else -> updatePassword(UpdatePasswordRequest(oldPassword, newPassword), dismissDialog)
        }
    }

    fun onNewUserImageClicked(userImage: UserImage) {
        userImageList.value?.let { userImages ->
            _userImageList.value = userImages.map { it.copy(isSelected = it.id == userImage.id) }
        }
    }

    fun updateProfileImage(profilePicId: Int) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            userRepository.updateProfilePic(UpdateProfilePicRequest(profilePicId))
                .onSuccess { _updateLoggedInUserData.postValue(Event(true)) }
                .onError { _, message -> message?.let { toast(it) } }
                .onException { throwable -> throwable.message?.let { toast(it) } }
            _isLoading.postValue(false)
        }
    }

    fun resetUserImageList() {
        userImageList.value?.let { userImages ->
            _userImageList.value = userImages.map {
                it.copy(isSelected = it.id == _loggedInUserData.value?.profilePicId)
            }
        }
    }

    private fun updatePassword(request: UpdatePasswordRequest, completion: (Boolean) -> Unit) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            userRepository.updatePassword(request)
                .onSuccess {
                    toast(it.message)
                    completion(true)
                }
                .onError { _, message ->
                    message?.let { toast(it) }
                    completion(false)
                }
                .onException { throwable ->
                    throwable.message?.let { toast(it) }
                    completion(false)
                }
            _isLoading.postValue(false)
        }
    }
}