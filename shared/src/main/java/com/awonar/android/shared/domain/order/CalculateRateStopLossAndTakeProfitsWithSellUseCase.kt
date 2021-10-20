package com.awonar.android.shared.domain.order

import com.awonar.android.model.order.Price
import com.awonar.android.model.order.RateStopLossRequest
import com.awonar.android.model.order.StopLossRequest
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.CurrenciesRepository
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/***
 * Amount Stoploss * conversion rate bid /unit - current price = - Stoploss Rate
 */
class CalculateRateStopLossAndTakeProfitsWithSellUseCase @Inject constructor(
    private val repository: CurrenciesRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<StopLossRequest, Price>(dispatcher) {

    override suspend fun execute(parameters: StopLossRequest): Price {
        val stopLoss = parameters.stopLoss
        val conversion = repository.getConversionByInstrumentId(parameters.instrumentId)
        val rate = (stopLoss.amount.times(conversion.rateBid).div(parameters.unit)
            .minus(parameters.openPrice)) * -1
        stopLoss.unit = rate
        return stopLoss
    }
}