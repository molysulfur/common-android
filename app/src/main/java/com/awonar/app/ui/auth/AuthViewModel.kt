package com.awonar.app.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.exception.UnAuthenticationIsExistEmailException
import com.awonar.android.model.Auth
import com.awonar.android.model.SignInFacebookRequest
import com.awonar.android.model.SignInGoogleRequest
import com.awonar.android.model.SignInRequest
import com.awonar.android.shared.domain.auth.*
import com.awonar.android.shared.utils.WhileViewSubscribed
import com.molysulfur.library.result.Result
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signInWithPasswordUseCase: SignInWithPasswordUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val signInWithFacebookUseCase: SignInWithFacebookUseCase,
    private val autoSignInUseCase: AutoSignInUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    val _navigation = Channel<String>(capacity = Channel.CONFLATED)
    val navigation get() = _navigation.receiveAsFlow()
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
                when (it) {
                    is Result.Success -> {
                        val data = it.successOr(null)
                        if (data != null) {
                            _signInState.value = data
                        } else {
                            _signInError.value = "Error"
                        }
                    }
                    is Result.Error -> {
                        _signInError.value = "Error"
                    }
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
                            _navigation.trySend("$email")
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

    fun signInWithFacebook(token: String?, userId: String?) {
        viewModelScope.launch {
            signInWithFacebookUseCase(
                SignInFacebookRequest(
                    token = token,
                    id = userId
                )
            ).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _signInState.value = result.data
                    }
                    is Result.Error -> {
                        if (result.exception is UnAuthenticationIsExistEmailException) {
                            _navigation.trySend("${(result.exception as UnAuthenticationIsExistEmailException).email}")
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

}