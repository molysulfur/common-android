package com.awonar.android.shared.domain.order

import com.awonar.android.model.order.OpenOrderRequest
import com.awonar.android.model.order.OpenOrderResponse
import com.awonar.android.model.portfolio.Position
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.OrderRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OpenOrderUseCase @Inject constructor(
    private val repository: OrderRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<OpenOrderRequest, Position?>(dispatcher) {
    override fun execute(parameters: OpenOrderRequest): Flow<Result<Position?>> =
        repository.openOrder(parameters)
}