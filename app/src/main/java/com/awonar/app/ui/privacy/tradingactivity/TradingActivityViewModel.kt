package com.awonar.app.ui.privacy.tradingactivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.tradingactivity.TradingActivityRequest
import com.awonar.android.shared.domain.tradingactivity.UpdateTradingActivityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TradingActivityViewModel @Inject constructor(
    private val updateTradingActivityUseCase: UpdateTradingActivityUseCase
) : ViewModel() {

    fun toggleTradingActivity(isPrivate: Boolean, isDisplayFullName: Boolean) {
        viewModelScope.launch {
            updateTradingActivityUseCase(
                TradingActivityRequest(
                    isPrivate = isPrivate,
                    isDisplayFullName = isDisplayFullName
                )
            )
        }
    }
}