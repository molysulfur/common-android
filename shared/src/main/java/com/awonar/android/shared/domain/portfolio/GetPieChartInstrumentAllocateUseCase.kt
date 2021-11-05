package com.awonar.android.shared.domain.portfolio

import com.awonar.android.model.portfolio.Position
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.PortfolioRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.result.successOr
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

class GetPieChartInstrumentAllocateUseCase @Inject constructor(
    private val portfolioRepository: PortfolioRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<String, Map<String, Double>>(dispatcher) {

    override fun execute(parameters: String): Flow<Result<Map<String, Double>>> = flow {
        portfolioRepository.getUserPortfolio().collect { result ->
            val position = result.successOr(null)
            val positionWithCategory =
                position?.positions?.filter { it.instrument.categories?.indexOf(parameters) ?: -1 >= 0 }
            val allocate = HashMap<String, Double>()
            val totalAmount =
                positionWithCategory
                    ?.sumOf { it.amount.toDouble() } ?: 0.0
            val positionByType: Map<String?, List<Position>> =
                positionWithCategory?.groupBy { it.instrument.symbol } ?: emptyMap()
            for ((k, v) in positionByType) {
                val allocateByType = v.sumOf { it.amount.toDouble() }
                allocate[k ?: ""] = allocateByType.div(totalAmount).times(100)
            }
            Timber.e("$allocate")
            emit(Result.Success(allocate))
        }
    }
}