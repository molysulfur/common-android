package com.awonar.app.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.Auth
import com.awonar.android.model.SignInRequest
import com.awonar.android.shared.domain.auth.AutoSignInUseCase
import com.awonar.android.shared.domain.auth.SignInWithPasswordUseCase
import com.awonar.android.shared.utils.WhileViewSubscribed
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    val signInWithPasswordUseCase: SignInWithPasswordUseCase,
    val autoSignInUseCase: AutoSignInUseCase
) : ViewModel() {


    private val _signInWithPasswordState = MutableStateFlow<Auth?>(null)
    val signInWithPasswordState: StateFlow<Auth?> get() = _signInWithPasswordState

    private val _signInError = MutableStateFlow("")
    val signInError: StateFlow<String> get() = _signInError

    val autoSignIn: StateFlow<Boolean?> = flow {
        autoSignInUseCase(Unit).collect {
            val isAuth: Boolean = it.successOr(false)
            emit(isAuth)
        }
    }.stateIn(viewModelScope, WhileViewSubscribed, null)

    fun signIn(username: String, password: String) {
        viewModelScope.launch {
            signInWithPasswordUseCase(SignInRequest(username, password)).collect {
                val data = it.successOr(null)
                if (data != null) {
                    _signInWithPasswordState.value = data
                } else {
                    _signInError.value = "Error"
                }
            }
        }
    }

}