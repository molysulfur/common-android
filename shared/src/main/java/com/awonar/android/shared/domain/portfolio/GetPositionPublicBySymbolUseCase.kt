package com.awonar.android.shared.domain.portfolio

import com.awonar.android.model.portfolio.PublicPosition
import com.awonar.android.model.portfolio.PublicPositionRequest
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.PortfolioRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPositionPublicBySymbolUseCase @Inject constructor(
    private val repository: PortfolioRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : FlowUseCase<PublicPositionRequest, PublicPosition?>(dispatcher) {
    override fun execute(parameters: PublicPositionRequest): Flow<Result<PublicPosition?>> =
        repository.getPositionPublic(parameters)
}