package com.awonar.app.ui.setting.privacy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.tradingactivity.TradingActivityRequest
import com.awonar.android.model.user.PersonalInfoResponse
import com.awonar.android.shared.domain.personal.GetPersonalInfoUseCase
import com.awonar.android.shared.domain.tradingactivity.UpdateTradingActivityUseCase
import com.awonar.android.shared.utils.WhileViewSubscribed
import com.molysulfur.library.result.data
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrivacyViewModel @Inject constructor(
    private val updateTradingActivityUseCase: UpdateTradingActivityUseCase,
    private val getPersonalInfoUseCase: GetPersonalInfoUseCase
) : ViewModel() {

    val personalState: StateFlow<PersonalInfoResponse?> = getPersonalInfoUseCase(Unit).mapLatest {
        it.successOr(null)
    }.stateIn(viewModelScope, WhileViewSubscribed, initialValue = null)

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