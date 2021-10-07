package com.awonar.android.shared.domain.order

import com.awonar.android.model.order.AmountStopLossRequest
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.CurrenciesRepository
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/***
 * Amount Stoploss =   current price - Stoploss Rate * unit / conversion rate bid
 */
class CalculateAmountStopLossWithSellUseCase @Inject constructor(
    private val repository: CurrenciesRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<AmountStopLossRequest, Float>(dispatcher) {

    override suspend fun execute(parameters: AmountStopLossRequest): Float {
        val conversion = repository.getConversionByInstrumentId(parameters.instrumentId)
        return parameters.openPrice.minus(parameters.stopLoss).times(parameters.unit)
            .div(conversion.rateBid)
    }
}