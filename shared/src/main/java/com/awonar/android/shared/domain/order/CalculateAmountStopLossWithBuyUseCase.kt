package com.awonar.android.shared.domain.order

import com.awonar.android.model.order.AmountStopLossRequest
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.CurrenciesRepository
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import timber.log.Timber
import javax.inject.Inject

class CalculateAmountStopLossWithBuyUseCase @Inject constructor(
    private val repository: CurrenciesRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<AmountStopLossRequest, Float>(dispatcher) {

    /***
     * Amount Stoploss = Stoploss Rate - current price * unit / conversion rate bid
     */
    override suspend fun execute(parameters: AmountStopLossRequest): Float {
        val conversion = repository.getConversionByInstrumentId(parameters.instrumentId)
        val data = parameters.stopLoss.minus(parameters.openPrice).times(parameters.unit)
            .div(conversion.rateBid)
        Timber.e("$conversion $parameters")
        return data
    }
}