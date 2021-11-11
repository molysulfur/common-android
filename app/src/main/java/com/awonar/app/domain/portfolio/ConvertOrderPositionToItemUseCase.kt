package com.awonar.app.domain.portfolio

import com.awonar.android.model.order.Price
import com.awonar.android.model.order.StopLossRequest
import com.awonar.android.model.portfolio.OrderPortfolio
import com.awonar.android.model.portfolio.PendingOrder
import com.awonar.android.model.portfolio.Position
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.domain.order.CalculateAmountStopLossAndTakeProfitWithBuyUseCase
import com.awonar.android.shared.domain.order.CalculateAmountStopLossAndTakeProfitWithSellUseCase
import com.awonar.android.shared.repos.CurrenciesRepository
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioItem
import com.molysulfur.library.result.data
import com.molysulfur.library.result.succeeded
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import timber.log.Timber
import javax.inject.Inject


class ConvertOrderPositionToItemUseCase @Inject constructor(
    private val currenciesRepository: CurrenciesRepository,
    private val calculateAmountStopLossAndTakeProfitWithBuyUseCase: CalculateAmountStopLossAndTakeProfitWithBuyUseCase,
    private val calculateAmountStopLossAndTakeProfitWithSellUseCase: CalculateAmountStopLossAndTakeProfitWithSellUseCase,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<List<PendingOrder>, List<OrderPortfolioItem>>(dispatcher) {
    override suspend fun execute(parameters: List<PendingOrder>): MutableList<OrderPortfolioItem> {
        val itemList = mutableListOf<OrderPortfolioItem>()
        parameters.forEach { position ->
            val conversionRate =
                currenciesRepository.getConversionByInstrumentId(position.instrumentId).rateBid
            val invested = position.amount
            val units = position.units
            val open = position.rate
            val leverage = position.leverage
            val fees = 0f
            val sl = position.stopLossRate
            val tp = position.takeProfitRate
            val amountSl = calculateAmount(position.instrumentId, sl, open, units, position.isBuy)
            val amountTp = calculateAmount(position.instrumentId, tp, open, units, position.isBuy)
            val slPercent = amountSl.times(100).div(invested)
            val tpPercent = amountTp.times(100).div(invested)
            val current = 0f // after get realtime
            val pl = 0f // cal after get realtime
            val plPercent = 0f // cal after get realtime
            val pipChange = 0f // cal after get realtime
            val value = 0f// cal after get realtime
            itemList.add(
                OrderPortfolioItem.InstrumentOrderItem(
                    position = position,
                    conversionRate = conversionRate,
                    invested = invested,
                    units = units,
                    open = open,
                    current = current,
                    stopLoss = sl,
                    takeProfit = tp,
                    profitLoss = pl,
                    profitLossPercent = plPercent,
                    pipChange = pipChange,
                    leverage = leverage,
                    value = value,
                    fees = fees,
                    amountStopLoss = amountSl,
                    amountTakeProfit = amountTp,
                    stopLossPercent = slPercent,
                    takeProfitPercent = tpPercent
                )
            )
        }
        return itemList
    }

    private suspend fun calculateAmount(
        instrumentId: Int,
        rate: Float,
        open: Float,
        unit: Float,
        isBuy: Boolean
    ): Float {
        val request = StopLossRequest(
            instrumentId = instrumentId,
            stopLoss = Price(0f, rate, "amount"),
            openPrice = open,
            unit = unit
        )
        val result = if (isBuy) {
            calculateAmountStopLossAndTakeProfitWithBuyUseCase(request)
        } else {
            calculateAmountStopLossAndTakeProfitWithSellUseCase(request)
        }
        if (result.succeeded) {
            return result.data?.amount ?: 0f
        }
        return 0f
    }
}
