package com.awonar.app.ui.withdraw

import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.awonar.android.model.payment.*
import com.awonar.android.model.portfolio.Portfolio
import com.awonar.android.shared.domain.payment.CreateWithdrawUseCase
import com.awonar.android.shared.domain.payment.GetMethodsPaymentUseCase
import com.awonar.android.shared.domain.payment.GetPaymentSettingUseCase
import com.awonar.android.shared.domain.payment.GetWithdrawOTPUseCase
import com.awonar.android.shared.domain.portfolio.GetMyPortFolioUseCase
import com.awonar.android.shared.utils.WhileViewSubscribed
import com.molysulfur.library.result.data
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@HiltViewModel
class WithdrawViewModel @Inject constructor(
    private val getMethodsPaymentUseCase: GetMethodsPaymentUseCase,
    private val getMyPortFolioUseCase: GetMyPortFolioUseCase,
    private val getPaymentSettingUseCase: GetPaymentSettingUseCase,
    private val getWithdrawOTPUseCase: GetWithdrawOTPUseCase,
    private val createWithdrawUseCase: CreateWithdrawUseCase
) : ViewModel() {

    private val _otp = MutableStateFlow<OTP?>(null)

    private val _methodId = MutableStateFlow("")

    private val _amount = MutableStateFlow(0f)
    val amount: StateFlow<Float> get() = _amount

    private val _navigationActions = Channel<NavDirections>(Channel.CONFLATED)
    val navigationActions get() = _navigationActions.receiveAsFlow()

    private val _paymentSetting: MutableStateFlow<PaymentSetting?> = MutableStateFlow(null)
    val paymentSetting: StateFlow<PaymentSetting?> get() = _paymentSetting

    val methodState: StateFlow<List<MethodPayment>> =
        getMethodsPaymentUseCase("withdraw").map { result ->
            val list: List<MethodPayment> = result.data ?: emptyList()
            list
        }.stateIn(viewModelScope, WhileViewSubscribed, emptyList())

    val portfolioState: StateFlow<Portfolio?> = flow {
        getMyPortFolioUseCase(true).collect {
            this.emit(it.successOr(null))
        }
    }.stateIn(viewModelScope, WhileViewSubscribed, null)

    fun getCurrencyForPayment(methodId: String) {
        viewModelScope.launch {
            getPaymentSettingUseCase(methodId).collect {
                _paymentSetting.value = it.successOr(null)
            }

        }

    }

    fun validate(): String {
        val number = _amount.value
        val portfolio = portfolioState.value
        val payment = _paymentSetting.value
        val maxWithdraw = payment?.maxWithdrawDollar ?: 0f
        val minWithdraw = payment?.minDepositDollar ?: 0f
        if ((number > maxWithdraw) or (number < minWithdraw)) {
            return "minimum withdraw is $%.2f and maximun withdraw is $%.2f".format(
                minWithdraw,
                maxWithdraw
            )
        }
        if (number > portfolio?.available ?: 0f) {
            return "Available is not enought"
        }
        viewModelScope.launch {
            _navigationActions.send(WithdrawBankingFragmentDirections.actionWithdrawBankingFragmentToWithdrawOTPFragment())
        }
        return ""
    }

    fun navigate(methodId: String) {
        viewModelScope.launch {
            _methodId.value = methodId
            _navigationActions.send(
                WithdrawMethodFragmentDirections.actionWithdrawMethodFragmentToWithdrawBankingFragment(
                    _methodId.value
                )
            )
        }
    }

    fun setAmount(number: Float) {
        _amount.value = number
    }

    fun requestOTP(view: View) {
        (view as AppCompatButton).isEnabled = false
        viewModelScope.launch {
            getWithdrawOTPUseCase(_amount.value).collect {
                val data = it.successOr(null)
                data?.let {
                    _otp.value = data
                    val countDown = data.exp.minus(Date().time.div(1000))
                    val timer = object : CountDownTimer(countDown, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            view.text = "Resend (%s)".format(millisUntilFinished / 1000)
                        }

                        override fun onFinish() {
                            view.isEnabled = true
                            view.text = "Resend"
                        }
                    }
                    timer.start()
                }
            }
        }
    }

    fun onSubmit(inputOTP: Int, bankId: String) {
        viewModelScope.launch {
            _navigationActions.send(
                WithdrawOTPFragmentDirections.actionWithdrawOTPFragmentToWithdrawSuccessFragment(
                    "12345",
                    _amount.value
                )
            )
//            val request = WithdrawRequest(
//                amount = _amount.value,
//                note = "",
//                verify = WithdrawVerify(otp = inputOTP, ref = _otp.value?.referenceNo1),
//                verifyBankAccountId = bankId
//            )
//            createWithdrawUseCase(request).collect { result ->
//                val data = result.successOr(null)
//                data?.let {
//                    _navigationActions.send(
//                        WithdrawOTPFragmentDirections.actionWithdrawOTPFragmentToWithdrawSuccessFragment(
//                            it.id ?: "",
//                            it.dollarAmount
//                        )
//                    )
//                }
//            }
        }
    }

}