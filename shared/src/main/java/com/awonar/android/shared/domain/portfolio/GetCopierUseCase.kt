package com.awonar.android.shared.domain.portfolio

import com.awonar.android.model.portfolio.Copier
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

class GetCopierUseCase @Inject constructor(
    private val portfolioRepository: PortfolioRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<String, Copier>(dispatcher) {
    override fun execute(parameters: String): Flow<Result<Copier>> = flow {
        portfolioRepository.getMyCopier().collect { result ->
            val list = result.successOr(emptyList())
            val position = list?.find { it.id == parameters }
            if (position != null)
                emit(Result.Success(position))
        }
    }
}