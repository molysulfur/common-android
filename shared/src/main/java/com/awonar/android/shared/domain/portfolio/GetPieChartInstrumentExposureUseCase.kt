package com.awonar.android.shared.domain.portfolio

import com.awonar.android.model.portfolio.Position
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

class GetPieChartInstrumentExposureUseCase @Inject constructor(
    private val portfolioRepository: PortfolioRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<String, Map<String, Double>>(dispatcher) {
    override fun execute(parameters: String): Flow<Result<Map<String, Double>>> = flow {
        portfolioRepository.getMyPositions().collect { result ->
            val position = result.successOr(null)?.filter { it.instrument?.categories?.indexOf(parameters) ?: -1 >= 0 }
            val totalExposure: Double = position?.sumOf { it.exposure.toDouble() } ?: 0.0
            val exposure = HashMap<String, Double>()
            val positionByType: Map<String?, List<Position>> =
                position?.groupBy { it.instrument?.symbol } ?: emptyMap()
            for ((k, v) in positionByType) {
                exposure[k ?: ""] =
                    v.sumOf { it.exposure.toDouble() }.div(totalExposure).times(100)
            }
            emit(Result.Success(exposure))
        }
    }
}