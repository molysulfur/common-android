package com.awonar.android.shared.domain.order

import com.awonar.android.model.order.AmountStopLossRequest
import com.awonar.android.model.order.RateStopLossRequest
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.CurrenciesRepository
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/***
 * Stoploss Rate = (Amount Stoploss * conversion rate bid / unit) + current price
 */
class CalculateRateStopLossWithBuyUseCase @Inject constructor(
    private val repository: CurrenciesRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<RateStopLossRequest, Float>(dispatcher) {

    override suspend fun execute(parameters: RateStopLossRequest): Float {
        val conversion = repository.getConversionByInstrumentId(parameters.instrumentId)
        return parameters.amountStopLoss.times(conversion.rateBid).div(parameters.unit)
            .plus(parameters.openPrice)
    }
}