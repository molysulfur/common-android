package com.awonar.app.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.exception.UnAuthenticationIsExistEmailException
import com.awonar.android.model.Auth
import com.awonar.android.model.SignInGoogleRequest
import com.awonar.android.model.SignInRequest
import com.awonar.android.shared.domain.auth.AutoSignInUseCase
import com.awonar.android.shared.domain.auth.SignInWithGoogleUseCase
import com.awonar.android.shared.domain.auth.SignInWithPasswordUseCase
import com.awonar.android.shared.domain.auth.SignOutUseCase
import com.awonar.android.shared.utils.WhileViewSubscribed
import com.molysulfur.library.result.Result
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signInWithPasswordUseCase: SignInWithPasswordUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val autoSignInUseCase: AutoSignInUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    val goToLinkAccountState = MutableStateFlow<String?>("")
    private val _goToSignUpState = MutableStateFlow(false)
    val goToSignUpState: StateFlow<Boolean> get() = _goToSignUpState
    private val _signInState = MutableStateFlow<Auth?>(null)
    val signInState: StateFlow<Auth?> get() = _signInState
    private val _signInError = MutableStateFlow("")
    val signInError: StateFlow<String> get() = _signInError
    private val _signInLoading = MutableStateFlow<Unit?>(null)
    val signInLoading: StateFlow<Unit?> get() = _signInLoading
    private val _signOutState = MutableStateFlow<Unit?>(null)
    val signOutState: StateFlow<Unit?> get() = _signOutState


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
                    _signInState.value = data
                } else {
                    _signInError.value = "Error"
                }
            }
        }
    }

    fun signInWithGoogle(email: String?, token: String?, id: String?) {
        viewModelScope.launch {
            signInWithGoogleUseCase(
                SignInGoogleRequest(
                    email = email,
                    token = token,
                    id = id
                )
            ).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _signInState.value = result.data
                    }
                    is Result.Error -> {
                        if (result.exception is UnAuthenticationIsExistEmailException) {
                            Timber.e("$result $email")
                            goToLinkAccountState.value = (email)
                        } else {
                            _goToSignUpState.value = true
                        }
                    }
                    is Result.Loading -> {

                    }
                }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            signOutUseCase(Unit).collect {
                Timber.e("$it")
                when (it) {
                    is Result.Success -> {
                        if (it.data)
                            _signOutState.value = Unit
                    }
                    is Result.Error -> {
                        _signInError.value = "${it.exception.message}"
                    }
                    is Result.Loading -> {
                        _signInLoading.value = Unit
                    }
                }
            }
        }
    }

}