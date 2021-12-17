package com.awonar.android.shared.domain.portfolio

import com.awonar.android.model.portfolio.Position
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.PortfolioRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.result.successOr
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetPieChartMarketAllocateUseCase @Inject constructor(
    private val portfolioRepository: PortfolioRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, Map<String, Double>>(dispatcher) {

    override fun execute(parameters: Unit): Flow<Result<Map<String, Double>>> = flow {
        portfolioRepository.getUserPortfolio().collect { result ->
            val position = result.successOr(null)
            val allocate = HashMap<String, Double>()
            val totalAmount = position?.positions?.sumOf { it.amount.toDouble() } ?: 0.0
            val positionByType: Map<String?, List<Position>> =
                position?.positions?.groupBy { it.instrument.categories?.get(0) } ?: emptyMap()
            for ((k, v) in positionByType) {
                val allocateByType = v.sumOf { it.amount.toDouble() }
                allocate[k ?: ""] = allocateByType.div(totalAmount).times(100)
            }
            emit(Result.Success(allocate))
        }
    }
}