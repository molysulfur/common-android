package com.awonar.android.shared.domain.order

import com.awonar.android.model.order.RateStopLossRequest
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.CurrenciesRepository
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/***
 * Amount Stoploss * conversion rate bid /unit - current price = - Stoploss Rate
 */
class CalculateRateStopLossWithSellUseCase @Inject constructor(
    private val repository: CurrenciesRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<RateStopLossRequest, Float>(dispatcher) {

    override suspend fun execute(parameters: RateStopLossRequest): Float {
        val conversion = repository.getConversionByInstrumentId(parameters.instrumentId)
        return (parameters.amountStopLoss.times(conversion.rateBid).div(parameters.unit)
            .minus(parameters.openPrice)) * -1
    }
}