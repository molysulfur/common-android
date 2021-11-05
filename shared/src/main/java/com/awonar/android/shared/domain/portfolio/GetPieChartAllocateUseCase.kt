package com.awonar.android.shared.domain.portfolio

import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.PortfolioRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.result.successOr
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetPieChartAllocateUseCase @Inject constructor(
    private val portfolioRepository: PortfolioRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, Map<String, Double>>(dispatcher) {

    override fun execute(parameters: Unit): Flow<Result<Map<String, Double>>> = flow {
        portfolioRepository.getUserPortfolio().collect { result ->
            val position = result.successOr(null)
            val portfolio = portfolioRepository.getPortFolio().last().successOr(null)
            val totalAmount = portfolio?.totalAllocated?.plus(portfolio.available) ?: 0f
            val balance: Double =
                portfolio?.available?.plus(position?.orders?.sumOf { it.amount.toDouble() }
                    ?: 0.0)?.div(totalAmount)?.times(100)
                    ?: 0.0
            val allocate = HashMap<String, Double>()
            allocate["balance"] = balance
            val allocateMarket: Double =
                position?.positions?.sumOf { it.amount.toDouble() }
                    ?.div(totalAmount)?.times(100)
                    ?: 0.0
            allocate["market"] = allocateMarket
            allocate["People"] = position?.copies?.sumOf { it.investAmount.toDouble() }
                ?.div(totalAmount)?.times(100) ?: 0.0
            emit(Result.Success(allocate))
        }
    }
}