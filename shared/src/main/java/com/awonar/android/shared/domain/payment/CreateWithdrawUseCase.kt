package com.awonar.android.shared.domain.payment

import com.awonar.android.model.payment.Withdraw
import com.awonar.android.model.payment.WithdrawRequest
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.PaymentRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateWithdrawUseCase @Inject constructor(
    private val repository: PaymentRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<WithdrawRequest, Withdraw?>(dispatcher) {

    override fun execute(parameters: WithdrawRequest): Flow<Result<Withdraw?>> =
        repository.requestWithdraw(parameters)
}