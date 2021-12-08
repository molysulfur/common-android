package com.awonar.android.shared.domain.partialclose

import com.awonar.android.model.order.ExitOrderPartialRequest
import com.awonar.android.model.portfolio.ExitOrder
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.OrderRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ClosePartialPositionUseCase @Inject constructor(
    private val repository: OrderRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<ExitOrderPartialRequest, ExitOrder?>(dispatcher) {
    override fun execute(parameters: ExitOrderPartialRequest): Flow<Result<ExitOrder?>> =
        repository.exitOrder(parameters)
}