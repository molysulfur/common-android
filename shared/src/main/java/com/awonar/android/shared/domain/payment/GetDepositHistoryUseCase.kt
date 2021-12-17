package com.awonar.android.shared.domain.payment

import com.awonar.android.model.payment.DepositHistoryResponse
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.PaymentRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDepositHistoryUseCase @Inject constructor(
    private val repository: PaymentRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<Int, DepositHistoryResponse?>(dispatcher) {
    override fun execute(parameters: Int): Flow<Result<DepositHistoryResponse?>> = repository.getDepositHistory(parameters)
}