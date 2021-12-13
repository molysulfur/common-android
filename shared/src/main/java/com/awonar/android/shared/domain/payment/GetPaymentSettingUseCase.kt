package com.awonar.android.shared.domain.payment

import com.awonar.android.model.payment.PaymentSetting
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.PaymentRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPaymentSettingUseCase @Inject constructor(
    private val repository: PaymentRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<String, PaymentSetting?>(dispatcher) {
    override fun execute(parameters: String): Flow<Result<PaymentSetting?>> =
        repository.getPaymentSetting(parameters)
}