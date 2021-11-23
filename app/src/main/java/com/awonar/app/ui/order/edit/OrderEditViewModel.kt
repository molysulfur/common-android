package com.awonar.app.ui.order.edit

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class OrderEditViewModel @Inject constructor() : ViewModel() {

    private val _takeProfitState = MutableStateFlow(Pair(0f, 0f))
    val takeProfitState get() = _takeProfitState
    private val _stopLossState = MutableStateFlow(Pair(0f, 0f))
    val stopLossState get() = _stopLossState
}