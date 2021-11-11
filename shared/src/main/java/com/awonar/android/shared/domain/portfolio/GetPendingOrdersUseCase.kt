package com.awonar.android.shared.domain.portfolio

import com.awonar.android.model.portfolio.OrderPortfolio
import com.awonar.android.model.portfolio.PendingOrder
import com.awonar.android.model.portfolio.Portfolio
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.PortfolioRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.result.successOr
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPendingOrdersUseCase @Inject constructor(
    private val repository: PortfolioRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, List<PendingOrder>>(dispatcher) {
    override fun execute(parameters: Unit): Flow<Result<List<PendingOrder>>> = flow {
        repository.getPendingOrders().collect {
            val data = it.successOr(null)
            if (data != null) {
                this.emit(Result.Success(data))
            }
        }

    }
}