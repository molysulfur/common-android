package com.awonar.app.ui.deposit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.awonar.android.model.payment.DepositRequest
import com.awonar.android.model.payment.MethodPayment
import com.awonar.android.model.payment.PaymentSetting
import com.awonar.android.model.payment.QRCode
import com.awonar.android.shared.domain.currency.GetCurrenciesRateUseCase
import com.awonar.android.shared.domain.currency.GetCurrenciesUseCase
import com.awonar.android.shared.domain.payment.GetDepositQrcodeUseCase
import com.awonar.android.shared.domain.payment.GetMethodsPaymentUseCase
import com.awonar.android.shared.domain.payment.GetPaymentSettingUseCase
import com.awonar.android.shared.utils.WhileViewSubscribed
import com.molysulfur.library.result.data
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DepositViewModel @Inject constructor(
    private val getCurrenciesRateUseCase: GetCurrenciesRateUseCase,
    private val getMethodsPaymentUseCase: GetMethodsPaymentUseCase,
    private val getPaymentSettingUseCase: GetPaymentSettingUseCase,
    private val getDepositQrcodeUseCase: GetDepositQrcodeUseCase,
    private val getCurrenciesUseCase: GetCurrenciesUseCase
) : ViewModel() {

    private val _methodId = MutableStateFlow("")
    val methodId: StateFlow<String> get() = _methodId
    private val _symbolName = MutableStateFlow("THB")
    val symbolName: StateFlow<String> get() = _symbolName
    private val _amount = MutableStateFlow(0f)
    val amount: StateFlow<Float> get() = _amount

    private val _qrcodeInfo = MutableStateFlow<QRCode?>(null)
    val qrcodeInfo: StateFlow<QRCode?> get() = _qrcodeInfo

    private val _navigationActions = Channel<NavDirections>(Channel.CONFLATED)
    val navigationActions get() = _navigationActions.receiveAsFlow()

    private val _currencyRate = MutableStateFlow(0f)
    val currencyRate: StateFlow<Float> get() = _currencyRate

    private val _paymentSetting: MutableStateFlow<PaymentSetting?> = MutableStateFlow(null)
    val paymentSetting: StateFlow<PaymentSetting?> get() = _paymentSetting

    val methodState: StateFlow<List<MethodPayment>> =
        getMethodsPaymentUseCase("deposit").map { result ->
            val list: List<MethodPayment> = result.data ?: emptyList()
            list
        }.stateIn(viewModelScope, WhileViewSubscribed, emptyList())

    fun getCurrencyForPayment(methodId: String) {
        viewModelScope.launch {
            getPaymentSettingUseCase(methodId).collect {
                _paymentSetting.value = it.successOr(null)
            }

        }

    }

    fun setAmount(amount: Float) {
        _amount.value = amount
    }

    fun navigateDepositFragment(id: String) {
        viewModelScope.launch {
            _navigationActions.send(
                MethodDepositFragmentDirections.actionMethodDepositFragmentToQRPaymentFragment(id)
            )
        }
    }

    fun getCurrencyRate(currencyName: String) {
        viewModelScope.launch {
            getCurrenciesRateUseCase(currencyName).collect {
                _currencyRate.value = it.successOr(0f)
            }
        }
    }

    fun getDepositQrcode(
        currencyId: String,
        methodId: String,
        redirectUrl: String
    ) {

        viewModelScope.launch {
            //TODO("Mock qrcode for test")
            _qrcodeInfo.value = QRCode(
                qrCode = "00020101021130830016A000000677010112011501055600681275502180000002112130001590318000R1500007100046653037645406501.005802TH5910GBPrimePay630479B1",
                amountUsd = 15.02f,
                localAmount = 501f,
                rate = 0.029984999999999998f,
                referenceNo = "R15000071000466",
                currencyId = "THB"
            )
            getDepositQrcodeUseCase(
                DepositRequest(
                    cardId = "",
                    amount = _amount.value,
                    amountUsd = _amount.value.times(_currencyRate.value),
                    cureencyId = currencyId,
                    methodId = methodId,
                    redirect = redirectUrl
                )
            ).collect {
                _qrcodeInfo.value = it.successOr(null)
            }
        }
    }

    fun validateMinMaxDeposit(symbol: String, methodId: String): String {
        _paymentSetting.value.let { paymentInfo ->
            val amountUsd = _amount.value.times(_currencyRate.value)
            val minDeposit = paymentInfo?.minDepositDollar ?: 0f
            val maxDeposit = paymentInfo?.maxDepositDollar ?: 0f
            if ((amountUsd < minDeposit) or (amountUsd > maxDeposit)) {
                return "minimum deposit is $%.2f and maximun deposit is $%.2f".format(
                    minDeposit,
                    maxDeposit
                )
            } else {
                viewModelScope.launch {
                    _symbolName.value = symbol
                    _methodId.value = methodId
                    _navigationActions.send(
                        QRPaymentFragmentDirections.actionQRPaymentFragmentToDepositConfirmFragment()
                    )
                }
                return ""
            }
        }
    }

}