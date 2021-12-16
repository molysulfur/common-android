package com.awonar.android.shared.domain.payment

import com.awonar.android.model.payment.WithdrawHistoryResponse
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.PaymentRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetWithdrawHistoryUseCase @Inject constructor(
    private val repository: PaymentRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<Int, WithdrawHistoryResponse?>(dispatcher) {
    override fun execute(parameters: Int): Flow<Result<WithdrawHistoryResponse?>> =
        repository.getWithdrawHistory(parameters)
}