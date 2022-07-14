package com.awonar.android.shared.domain.order

import com.awonar.android.model.order.MinMaxStopLossRequest
import com.awonar.android.model.order.MinMaxTakeProfitRequest
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.CurrenciesRepository
import com.awonar.android.shared.repos.MarketRepository
import com.awonar.android.shared.steaming.QuoteSteamingManager
import com.awonar.android.shared.utils.ConverterOrderUtil
import com.awonar.android.shared.utils.ConverterQuoteUtil
import com.awonar.android.shared.utils.PortfolioUtil
import com.molysulfur.library.result.successOr
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import timber.log.Timber
import javax.inject.Inject

class GetMinMaxTakeProfitUseCase @Inject constructor(
    private val repository: CurrenciesRepository,
    private val marketRepository: MarketRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<MinMaxTakeProfitRequest, Pair<Float, Float>>(dispatcher) {
    override suspend fun execute(parameters: MinMaxTakeProfitRequest): Pair<Float, Float> {
        val isBuy = parameters.isBuy
        val amount = parameters.amount
        val units = parameters.units
        val instrumentId = parameters.instrumentId
        val openRate = parameters.openRate
        val leverage = parameters.leverage
        val conversion = repository.getConversionByInstrumentId(instrumentId).rateBid
        val tradingData = marketRepository.getTradingDataById(instrumentId)
        val quote = QuoteSteamingManager.quotesState.value[instrumentId]
        var response = Pair(0f, 0f)
        quote?.let {
            val current = ConverterQuoteUtil.getCurrentPrice(it, leverage, isBuy)
            val pl = PortfolioUtil.getProfitOrLoss(
                current,
                openRate,
                units,
                conversion,
                isBuy
            )
            val value = pl.plus(amount)
            val minRateTp = if (isBuy) quote.bid else quote.ask
            val maxAmountTp = value.times(tradingData.maxTakeProfitPercentage)
                .div(100)
            val minAmountTp =
                minRateTp.minus(current).times(amount).div(conversion)
            response = Pair(minAmountTp, maxAmountTp)
        }
        return response
    }
}