package com.awonar.app.ui.user

import android.view.View
import androidx.databinding.Bindable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.user.User
import com.awonar.android.model.user.UserRequest
import com.awonar.android.shared.domain.profile.GetUserProfileUseCase
import com.awonar.android.shared.domain.user.GetUserUseCase
import com.awonar.app.ui.setting.TestRepo
import com.molysulfur.library.result.data
import com.molysulfur.library.result.succeeded
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase
) : ViewModel() {

    private val _userState = MutableStateFlow<User?>(null)
    val userState: MutableStateFlow<User?> get() = _userState

    /**
     * Loading names from the repo async on the background thread and updating the StateFlow
     * ether with data received in the case of success or an error message.
     */
    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                TestRepo().loadFriendsNames()
                    .combine(userState) { _, user ->
                        user
                    }
                    .flowOn(Dispatchers.IO)
                    .collect {
                        _userState.value = it
                    }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

    }

    fun getUser(needFresh: Boolean) {
        viewModelScope.launch {
            getUserUseCase(UserRequest(needFresh = needFresh)).collect {
                if (it.succeeded) {
                    _userState.value = it.data
                }
            }
        }
    }

    fun getUser(userId: String) {
        viewModelScope.launch {
            getUserProfileUseCase(UserRequest(userId = userId)).collect {
                if (it.succeeded) {
                    _userState.value = it.data
                }
            }
        }
    }

    fun editAboutMe() {
        Timber.e(userState.value.toString())
    }


}