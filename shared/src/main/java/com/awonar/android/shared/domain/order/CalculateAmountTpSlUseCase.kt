package com.awonar.android.shared.domain.order

import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.CurrenciesRepository
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/***
 * isBuy = true
 * Amount Stoploss = (Stoploss Rate - current price) * unit / conversion rate bid
 *
 * isBuy = false
 * Amount Stoploss =  (current price - Stoploss Rate) * unit / conversion rate bid
 */
class CalculateAmountTpSlUseCase @Inject constructor(
    private val repository: CurrenciesRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<TpSlRequest, Pair<Float, Float>>(dispatcher) {
    override suspend fun execute(parameters: TpSlRequest): Pair<Float, Float> {
        val tpsl = parameters.tpsl
        val conversion = repository.getConversionByInstrumentId(parameters.instrumentId)
        val amount = if (parameters.isBuy) {
            tpsl.second.minus(parameters.current).times(parameters.unit).div(conversion.rateBid)
        } else {
            parameters.current.minus(tpsl.second).times(parameters.unit).div(conversion.rateBid)
        }
        return Pair(amount, tpsl.second)
    }
}

data class TpSlRequest(
    val tpsl: Pair<Float, Float>,
    val current: Float,
    val unit: Float,
    val instrumentId: Int,
    val isBuy: Boolean
)