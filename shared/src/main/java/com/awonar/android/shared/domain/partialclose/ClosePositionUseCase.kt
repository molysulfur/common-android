package com.awonar.android.shared.domain.partialclose

import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.OrderRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ClosePositionUseCase @Inject constructor(
    private val repository: OrderRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<String, Any?>(dispatcher) {
    override fun execute(parameters: String): Flow<Result<Any?>> = repository.exitOrder(parameters)
}