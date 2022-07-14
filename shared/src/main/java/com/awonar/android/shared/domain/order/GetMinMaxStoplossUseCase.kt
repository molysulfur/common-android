package com.awonar.android.shared.domain.order

import com.awonar.android.model.order.MinMaxStopLossRequest
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.di.MainDispatcher
import com.awonar.android.shared.repos.CurrenciesRepository
import com.awonar.android.shared.repos.MarketRepository
import com.awonar.android.shared.steaming.QuoteSteamingManager
import com.awonar.android.shared.utils.ConverterOrderUtil
import com.molysulfur.library.result.successOr
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import timber.log.Timber
import javax.inject.Inject

class GetMinMaxStoplossUseCase @Inject constructor(
    private val repository: CurrenciesRepository,
    private val marketRepository: MarketRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<MinMaxStopLossRequest, Pair<Float, Float>>(dispatcher) {
    override suspend fun execute(parameters: MinMaxStopLossRequest): Pair<Float, Float> {
        val isBuy = parameters.isBuy
        val units = parameters.units
        val amount = parameters.amount
        val instrumentId = parameters.instrumentId
        val leverage = parameters.leverage
        val conversion = repository.getConversionByInstrumentId(instrumentId).rateBid
        val tradingData = marketRepository.getTradingDataById(instrumentId)
        val quote = QuoteSteamingManager.quotesState.value[instrumentId]
        var response = Pair(0f, 0f)
        quote?.let {
            val minRate =
                if (isBuy) quote.bid.minus(quote.ask) else quote.ask.minus(quote.bid)
            val minAmountSl = (minRate).times(units).div(conversion)
            val maxAmountSl = ConverterOrderUtil.getMaxAmountSl(
                native = amount,
                leverage = leverage,
                isBuy = isBuy,
                tradingData = tradingData
            )
            response = Pair(-minAmountSl, -maxAmountSl)
        }
        return response
    }
}