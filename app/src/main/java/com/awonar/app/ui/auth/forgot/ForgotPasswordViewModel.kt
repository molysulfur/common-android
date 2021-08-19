package com.awonar.app.ui.auth.forgot

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.shared.domain.auth.ForgotPasswordUseCase
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val forgotPasswordUseCase: ForgotPasswordUseCase
) : ViewModel() {

    var submitState = MutableStateFlow<String?>(null)

    fun submit(view: View) {
        viewModelScope.launch {
            val result = forgotPasswordUseCase("")
            submitState.value = "${result.successOr("")}"
        }
    }
}