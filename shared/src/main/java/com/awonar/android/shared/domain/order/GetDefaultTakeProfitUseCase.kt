package com.awonar.android.shared.domain.order

import com.awonar.android.model.order.DefaultStopLossRequest
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.MarketRepository
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import timber.log.Timber
import javax.inject.Inject

class GetDefaultTakeProfitUseCase @Inject constructor(
    private val repository: MarketRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<DefaultStopLossRequest, Float>(dispatcher) {
    override suspend fun execute(parameters: DefaultStopLossRequest): Float {
        val trading = repository.getTradingDataById(parameters.instrumentId)
        val percent = (trading.defaultTakeProfitPercentage.minus(0.5f).div(100))
        return parameters.amount.times(percent)
    }
}