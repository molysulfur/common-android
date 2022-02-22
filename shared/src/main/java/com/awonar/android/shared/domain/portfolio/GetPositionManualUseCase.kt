package com.awonar.android.shared.domain.portfolio

import com.awonar.android.model.portfolio.UserPortfolioResponse
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.PortfolioRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPositionManualUseCase @Inject constructor(
    private val repository: PortfolioRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : FlowUseCase<String?, UserPortfolioResponse?>(dispatcher) {
    override fun execute(parameters: String?): Flow<Result<UserPortfolioResponse?>> =
        if (parameters.isNullOrBlank()) {
            repository.getUserPortfolio()
        } else {
            repository.getUserPortfolio(parameters)
        }
}