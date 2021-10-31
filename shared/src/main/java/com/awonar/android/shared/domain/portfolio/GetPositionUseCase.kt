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

class GetPositionUseCase @Inject constructor(
    private val portfolioRepository: PortfolioRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<String, List<Position>>(dispatcher) {
    override fun execute(parameters: String): Flow<Result<List<Position>>> = flow {
        portfolioRepository.getMyPositions().collect { result ->
            val list = result.successOr(emptyList())
            val instrumentId = list?.find { it.id == parameters }?.instrumentId
            val position = list?.filter { it.instrumentId == instrumentId }
            if (position != null)
                emit(Result.Success(position))
        }
    }
}