package com.awonar.app.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.Auth
import com.awonar.android.model.SignInRequest
import com.awonar.android.shared.domain.auth.SignInWithPasswordUseCase
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    val signInWithPasswordUseCase: SignInWithPasswordUseCase
) : ViewModel() {

    private val _signInWithPasswordState = MutableStateFlow<Auth?>(null)
    val signInWithPasswordState: StateFlow<Auth?> get() = _signInWithPasswordState

    fun testViewModel() {
        viewModelScope.launch {
            signInWithPasswordUseCase(SignInRequest("", "")).collect {
                val data = it.successOr(null)
                Timber.e("$data")
                _signInWithPasswordState.value = data
            }
        }
    }
}