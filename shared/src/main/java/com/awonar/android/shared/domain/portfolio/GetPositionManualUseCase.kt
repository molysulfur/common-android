package com.awonar.android.shared.domain.portfolio

import com.awonar.android.model.portfolio.UserPortfolioResponse
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.MarketRepository
import com.awonar.android.shared.repos.PortfolioRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.result.succeeded
import com.molysulfur.library.result.successOr
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPositionManualUseCase @Inject constructor(
    private val repository: PortfolioRepository,
    private val marketRepository: MarketRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : FlowUseCase<String?, UserPortfolioResponse?>(dispatcher) {
    override fun execute(parameters: String?): Flow<Result<UserPortfolioResponse?>> = flow {
        if (parameters.isNullOrBlank()) {
            getMyPortfolio(this)
        } else {
            getOtherPortfolio(parameters, this)
        }
    }

    private suspend fun getMyPortfolio(flow: FlowCollector<Result<UserPortfolioResponse?>>) {
        repository.getUserPortfolio().collect {
            if (it.succeeded) {
                flow.emit(Result.Success(it.successOr(null)))
            }
        }
    }

    private suspend fun getOtherPortfolio(
        parameters: String,
        flow: FlowCollector<Result<UserPortfolioResponse?>>,
    ) {
        repository.getUserPortfolio(parameters).collect {
            if (it.succeeded) {
                val data = it.successOr(null)
                data?.positions?.forEach { position ->
                    position.copy(instrument = marketRepository.getInstrumentFromDao(position.instrumentId))
                }
                flow.emit(Result.Success(data))
            }
        }
    }
}