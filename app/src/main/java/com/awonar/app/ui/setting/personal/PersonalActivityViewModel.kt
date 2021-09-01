package com.awonar.app.ui.setting.personal

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PersonalActivityViewModel @Inject constructor() : ViewModel() {

    val pageState = MutableStateFlow(0)

    fun onPageChange(position: Int) {
        pageState.value = position
    }
}