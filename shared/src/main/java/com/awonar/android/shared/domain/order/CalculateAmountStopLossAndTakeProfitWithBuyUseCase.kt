package com.awonar.android.shared.domain.order

import com.awonar.android.model.order.StopLossRequest
import com.awonar.android.model.order.Price
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.CurrenciesRepository
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/***
 * Amount Stoploss = Stoploss Rate - current price * unit / conversion rate bid
 */
class CalculateAmountStopLossAndTakeProfitWithBuyUseCase @Inject constructor(
    private val repository: CurrenciesRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<StopLossRequest, Price>(dispatcher) {

    override suspend fun execute(parameters: StopLossRequest): Price {
        val stoploss = parameters.stopLoss
        val conversion = repository.getConversionByInstrumentId(parameters.instrumentId)
        val amount = stoploss.unit.minus(parameters.openPrice).times(parameters.unit)
            .div(conversion.rateBid)
        stoploss.amount = amount
        return stoploss
    }
}