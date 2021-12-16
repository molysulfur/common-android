package com.awonar.app.ui.payment.deposit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.awonar.android.model.payment.*
import com.awonar.android.shared.domain.currency.GetCurrenciesRateUseCase
import com.awonar.android.shared.domain.payment.GetDepositHistoryUseCase
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
    private val getDepositHistoryUseCase: GetDepositHistoryUseCase,
) : ViewModel() {

    private val _historyState = MutableStateFlow<List<Deposit>>(emptyList())
    val historyState: StateFlow<List<Deposit>> get() = _historyState

    private val _methodId = MutableStateFlow("")
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

    private val _page = MutableStateFlow(0)
    val page: StateFlow<Int> get() = _page

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
            when (id) {
                "history" -> _navigationActions.send(MethodDepositFragmentDirections.actionMethodDepositFragmentToDepositHistoryFragment())
                else -> _navigationActions.send(
                    MethodDepositFragmentDirections.actionMethodDepositFragmentToQRPaymentFragment(
                        id
                    )
                )
            }

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
            return if ((amountUsd < minDeposit) or (amountUsd > maxDeposit)) {
                "minimum deposit is $%.2f and maximun deposit is $%.2f".format(
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
                ""
            }
        }
    }

    fun getHistory() {
        viewModelScope.launch {
            getDepositHistoryUseCase(_page.value).collect { result ->
                val data = result.successOr(null)
                _page.value = if (data?.meta?.hasMore == true) {
                    data.meta.page + 1
                } else {
                    0
                }
                val list = _historyState.value.toMutableList()
                list.addAll(data?.histories ?: emptyList())
                _historyState.value = list
            }
        }
    }

}