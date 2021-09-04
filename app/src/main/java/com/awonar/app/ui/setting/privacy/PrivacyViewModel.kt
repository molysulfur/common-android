package com.awonar.app.ui.setting.privacy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.bookbank.BookBank
import com.awonar.android.model.bookbank.BookBankRequest
import com.awonar.android.model.privacy.PersonalAddressRequest
import com.awonar.android.model.privacy.PersonalCardIdRequest
import com.awonar.android.model.privacy.PersonalProfileRequest
import com.awonar.android.model.tradingactivity.TradingActivityRequest
import com.awonar.android.model.user.PersonalInfoResponse
import com.awonar.android.shared.domain.bookbank.GetBookBankUseCase
import com.awonar.android.shared.domain.bookbank.UpdateVerifyBookBankUseCase
import com.awonar.android.shared.domain.personal.GetPersonalInfoUseCase
import com.awonar.android.shared.domain.personal.UpdateVerifyAddressUseCase
import com.awonar.android.shared.domain.personal.UpdateVerifyCardIdUseCase
import com.awonar.android.shared.domain.personal.UpdateVerifyProfileUseCase
import com.awonar.android.shared.domain.tradingactivity.UpdateTradingActivityUseCase
import com.awonar.android.shared.utils.WhileViewSubscribed
import com.molysulfur.library.result.Result
import com.molysulfur.library.result.data
import com.molysulfur.library.result.succeeded
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject

@HiltViewModel
class PrivacyViewModel @Inject constructor(
    private val updateTradingActivityUseCase: UpdateTradingActivityUseCase,
    private val getPersonalInfoUseCase: GetPersonalInfoUseCase,
    private val getBookBankUseCase: GetBookBankUseCase,
    private val updateVerifyProfileUseCase: UpdateVerifyProfileUseCase,
    private val updateVerifyAddressUseCase: UpdateVerifyAddressUseCase,
    private val updateVerifyCardIdUseCase: UpdateVerifyCardIdUseCase,
    private val updateVerifyBookBankUseCase: UpdateVerifyBookBankUseCase
) : ViewModel() {

    val bookBankState: StateFlow<BookBank?> = getBookBankUseCase(Unit).map { result ->
        result.data
    }.stateIn(viewModelScope, WhileViewSubscribed, null)

    private val _personalState: MutableStateFlow<PersonalInfoResponse?> = MutableStateFlow(null)
    val personalState: StateFlow<PersonalInfoResponse?> = _personalState

    private val _toastMessageState = MutableStateFlow<String?>("")
    val toastMessageState get() = _toastMessageState

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

    fun updateAddress(country: String?, city: String?, postal: String?, address: String?) {
        viewModelScope.launch {
            updateVerifyAddressUseCase(
                PersonalAddressRequest(
                    address = address,
                    country = country,
                    city = city,
                    postalCode = postal
                )
            )
        }
    }

    fun updateVerifyCardId(idCard: String, idType: Int, docs: ArrayList<String?>) {
        viewModelScope.launch {
            updateVerifyCardIdUseCase(
                PersonalCardIdRequest(
                    idDocs = docs,
                    idNo = idCard,
                    idType = idType
                )
            ).collect {
                if (it.succeeded) {
                    _personalState.value = it.data
                } else if (it is Result.Error) {
                    _toastMessageState.value = it.exception.message
                }
            }
        }
    }

    fun uploadBookBank(
        accountName: String,
        accountNumber: String,
        country: String,
        bank: String,
        accountType: String,
        image: String
    ) {
        viewModelScope.launch {
            updateVerifyBookBankUseCase(
                BookBankRequest(
                    bank = bank,
                    bookBankImage = arrayListOf(image),
                    countryId = country,
                    name = accountName,
                    number = accountNumber,
                    type = accountType
                )
            ).collect {
                if (it.succeeded) {
                    _toastMessageState.value = "Upload is Finished."
                }
            }
        }
    }


}