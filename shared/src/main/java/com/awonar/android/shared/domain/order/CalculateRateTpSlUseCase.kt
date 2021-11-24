package com.awonar.android.shared.domain.order

import com.awonar.android.model.order.Price
import com.awonar.android.model.order.StopLossRequest
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.CurrenciesRepository
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * isBuy = true
 * Rate = (Amount Stoploss * conversion rate bid / unit) + current price
 *
 * isBuy = fals
 *  Rate = -(Amount Stoploss * conversion rate bid /unit - current price)
 */
class CalculateRateTpSlUseCase @Inject constructor(
    private val repository: CurrenciesRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<TpSlRequest, Pair<Float, Float>>(dispatcher) {
    override suspend fun execute(parameters: TpSlRequest): Pair<Float, Float> {
        val conversionRate = repository.getConversionByInstrumentId(parameters.instrumentId).rateBid
        val amount = parameters.tpsl.first
        val rate = if (parameters.isBuy) {
            amount.times(conversionRate).div(parameters.unit).plus(parameters.current)
        } else {
            -(amount.times(conversionRate).div(parameters.unit).minus(parameters.current))
        }
        return Pair(amount, rate)
    }

}