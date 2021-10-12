package com.awonar.android.shared.domain.order

import com.awonar.android.model.order.Price
import com.awonar.android.model.order.RateStopLossRequest
import com.awonar.android.model.order.StopLossRequest
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.CurrenciesRepository
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import timber.log.Timber
import javax.inject.Inject

/***
 * Stoploss Rate = (Amount Stoploss * conversion rate bid / unit) + current price
 */
class CalculateRateStopLossAndTakeProfitWithBuyUseCase @Inject constructor(
    private val repository: CurrenciesRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<StopLossRequest, Price>(dispatcher) {

    override suspend fun execute(parameters: StopLossRequest): Price {
        val stopLoss = parameters.stopLoss
        val conversion = repository.getConversionByInstrumentId(parameters.instrumentId)
        val rate = (stopLoss.amount.times(conversion.rateBid).div(parameters.unit))
            .plus(parameters.openPrice)
        return Price(stopLoss.amount, rate, stopLoss.type)
    }
}