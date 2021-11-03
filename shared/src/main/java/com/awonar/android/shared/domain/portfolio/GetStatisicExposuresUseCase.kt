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

class GetStatisicExposuresUseCase @Inject constructor(
    private val portfolioRepository: PortfolioRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, Map<String, Double>>(dispatcher) {
    override fun execute(parameters: Unit): Flow<Result<Map<String, Double>>> = flow {
        portfolioRepository.getMyPositions().collect { result ->
            val data: List<Position> = result.successOr(emptyList()) ?: emptyList()
            val totalExposure: Double = data.sumOf { it.exposure.toDouble() }
            val exposureMap = HashMap<String, Double>()
            val exposureGroupWithType: Map<String, List<Position>> =
                data.groupBy { it.instrument.categories?.get(0) ?: "N/A" }
            for ((k, v) in exposureGroupWithType) {
                exposureMap[k] = v.sumOf { it.exposure.toDouble() }.div(totalExposure).times(100)
            }
            emit(Result.Success(exposureMap))
        }
    }
}