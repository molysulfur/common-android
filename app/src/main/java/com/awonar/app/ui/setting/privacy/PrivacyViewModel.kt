package com.awonar.app.ui.setting.privacy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.privacy.PersonalProfileRequest
import com.awonar.android.model.tradingactivity.TradingActivityRequest
import com.awonar.android.model.user.PersonalInfoResponse
import com.awonar.android.shared.domain.personal.GetPersonalInfoUseCase
import com.awonar.android.shared.domain.personal.UpdateVerifyProfileUseCase
import com.awonar.android.shared.domain.tradingactivity.UpdateTradingActivityUseCase
import com.molysulfur.library.result.data
import com.molysulfur.library.result.succeeded
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrivacyViewModel @Inject constructor(
    private val updateTradingActivityUseCase: UpdateTradingActivityUseCase,
    private val getPersonalInfoUseCase: GetPersonalInfoUseCase,
    private val updateVerifyProfileUseCase: UpdateVerifyProfileUseCase
) : ViewModel() {
    private val _personalState: MutableStateFlow<PersonalInfoResponse?> = MutableStateFlow(null)
    val personalState: StateFlow<PersonalInfoResponse?> = _personalState

    init {
       viewModelScope.launch {
           getPersonalInfoUseCase(Unit).collect {
               _personalState.value = it.successOr(null)
           }
       }
    }

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

    fun updatePersonalProfile(
        name: String?,
        middleName: String?,
        lastName: String?,
        gender: Boolean,
        birth: String?
    ) {
        viewModelScope.launch {
            updateVerifyProfileUseCase(
                PersonalProfileRequest(
                    firstName = name,
                    middleName = middleName,
                    lastName = lastName,
                    birthDate = birth,
                    gender = if (gender) 0 else 1
                )
            ).collect {
                if (it.succeeded) {
                    _personalState.value = it.data
                }
            }
        }
    }
}