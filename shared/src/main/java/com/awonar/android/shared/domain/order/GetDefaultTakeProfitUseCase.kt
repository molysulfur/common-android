package com.awonar.android.shared.domain.order

import com.awonar.android.model.order.DefaultStopLossRequest
import com.awonar.android.model.order.Price
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.CurrenciesRepository
import com.awonar.android.shared.repos.MarketRepository
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import timber.log.Timber
import javax.inject.Inject

/**
 * amount_tp * conversion bid / unit + open_rate = (tp_rate)
 */

class GetDefaultTakeProfitUseCase @Inject constructor(
    private val repository: MarketRepository,
    private val currenciesRepository: CurrenciesRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<DefaultStopLossRequest, Price>(dispatcher) {
    override suspend fun execute(parameters: DefaultStopLossRequest): Price {
        val trading = repository.getTradingDataById(parameters.instrumentId)
        val conversion = currenciesRepository.getConversionByInstrumentId(parameters.instrumentId)
        val percent = (trading.defaultTakeProfitPercentage.minus(0.5f).div(100))
        val amountTp = parameters.amount.times(percent)
        val rateTp = amountTp.times(conversion.rateBid).div(parameters.unit).plus(parameters.price)
        return Price(amount = amountTp, unit = rateTp, type = "amount")
    }
}