package com.awonar.android.shared.domain.order

import com.awonar.android.model.order.UpdateOrderRequest
import com.awonar.android.model.portfolio.ExitOrder
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.OrderRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.result.succeeded
import com.molysulfur.library.result.successOr
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateOrderUseCase @Inject constructor(
    private val repository: OrderRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<UpdateOrderRequest, ExitOrder?>(dispatcher) {
    override fun execute(parameters: UpdateOrderRequest): Flow<Result<ExitOrder?>> = flow {
        repository.editOrder(parameters).collect {
            if (it.succeeded) {
                emit(Result.Success(it.successOr(null)?.exitOrder))
            }
        }
    }
}