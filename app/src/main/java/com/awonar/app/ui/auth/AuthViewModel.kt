package com.awonar.app.ui.auth

import androidx.lifecycle.ViewModel
import com.awonar.android.model.SignInRequest
import com.awonar.android.shared.domain.auth.SignInWithPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    val signInWithPasswordUseCase: SignInWithPasswordUseCase
) : ViewModel() {

    fun testViewModel() {
        signInWithPasswordUseCase(SignInRequest("", "")).map {
            Timber.e("$it")
        }
    }
}