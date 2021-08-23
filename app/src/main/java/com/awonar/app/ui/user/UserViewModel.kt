package com.awonar.app.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.user.User
import com.awonar.android.model.user.UserRequest
import com.awonar.android.shared.domain.user.GetUserUseCase
import com.molysulfur.library.result.data
import com.molysulfur.library.result.succeeded
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    private val _userState = MutableStateFlow<User?>(null)
    val userState: StateFlow<User?> get() = _userState

    fun getUser(needFresh: Boolean) {
        viewModelScope.launch {
            getUserUseCase(UserRequest(needFresh)).collect {
                if (it.succeeded) {
                    _userState.value = it.data
                }
            }
        }
    }

}