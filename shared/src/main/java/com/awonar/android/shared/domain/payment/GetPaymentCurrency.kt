package com.awonar.android.shared.domain.payment

import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.PaymentRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPaymentCurrency @Inject constructor(
    private val paymentRepository: PaymentRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<String, Unit>(dispatcher) {
    override fun execute(parameters: String): Flow<Result<Unit>> {
        TODO("Not yet implemented")
    }

}