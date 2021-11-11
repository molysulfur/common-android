package com.awonar.android.shared.domain.portfolio

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
import timber.log.Timber
import javax.inject.Inject

class GetMyPortFolioUseCase @Inject constructor(
    private val repository: PortfolioRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<Boolean, Portfolio>(dispatcher) {
    override fun execute(parameters: Boolean): Flow<Result<Portfolio>> = flow {
        repository.getPortFolio().collect {
            val data = it.successOr(null)
            if (data != null) {
                this.emit(Result.Success(data))
            }
        }

    }
}