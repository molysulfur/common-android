package com.awonar.app.ui.deposit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.awonar.android.model.payment.MethodPayment
import com.awonar.android.shared.domain.payment.GetMethodsPaymentUseCase
import com.awonar.android.shared.utils.WhileViewSubscribed
import com.molysulfur.library.result.data
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DepositViewModel @Inject constructor(
    private val getMethodsPaymentUseCase: GetMethodsPaymentUseCase
) : ViewModel() {

    val _navigationState = Channel<NavDirections>(Channel.CONFLATED)
    val navigationState get() = _navigationState.receiveAsFlow()

    val methodState: StateFlow<List<MethodPayment>> =
        getMethodsPaymentUseCase("deposit").map { result ->
            val list: List<MethodPayment> = result.data ?: emptyList()
            list
        }.stateIn(viewModelScope, WhileViewSubscribed, emptyList())


    fun navigateDepositFragment(id: String) {
        viewModelScope.launch {
            when (id) {
                "qr payment" -> _navigationState.send(MethodDepositFragmentDirections.actionMethodDepositFragmentToQRPaymentFragment())
            }
        }
    }

}