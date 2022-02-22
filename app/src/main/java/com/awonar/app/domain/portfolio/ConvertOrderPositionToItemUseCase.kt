package com.awonar.app.domain.portfolio

import com.awonar.android.model.order.Price
import com.awonar.android.model.order.StopLossRequest
import com.awonar.android.model.portfolio.PendingOrder
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.domain.order.CalculateAmountStopLossAndTakeProfitWithBuyUseCase
import com.awonar.android.shared.domain.order.CalculateAmountStopLossAndTakeProfitWithSellUseCase
import com.awonar.android.shared.repos.CurrenciesRepository
import com.awonar.app.ui.portfolio.adapter.PortfolioItem
import com.molysulfur.library.result.data
import com.molysulfur.library.result.succeeded
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject


class ConvertOrderPositionToItemUseCase @Inject constructor(
    private val currenciesRepository: CurrenciesRepository,
    private val calculateAmountStopLossAndTakeProfitWithBuyUseCase: CalculateAmountStopLossAndTakeProfitWithBuyUseCase,
    private val calculateAmountStopLossAndTakeProfitWithSellUseCase: CalculateAmountStopLossAndTakeProfitWithSellUseCase,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<List<PendingOrder>, List<PortfolioItem>>(dispatcher) {
    override suspend fun execute(parameters: List<PendingOrder>): MutableList<PortfolioItem> {
        val itemList = mutableListOf<PortfolioItem>()
        parameters.forEach { position ->
//            with(position) {
//                conversionRate =
//                    currenciesRepository.getConversionByInstrumentId(position.instrumentId).rateBid
//                invested = position.amount
//                units = position.units
//                open = position.rate
//                leverage = position.leverage
//                sl = position.stopLossRate
//                tp = position.takeProfitRate
//                amountSl =
//                    calculateAmount(position.instrumentId, sl, open, units, position.isBuy)
//                amountTp =
//                    calculateAmount(position.instrumentId, tp, open, units, position.isBuy)
//                slPercent = amountSl.times(100).div(invested)
//                tpPercent = amountTp.times(100).div(invested)
//            }
//            itemList.add(
//                PortfolioItem.InstrumentItem(
//                    position = position,
//                    conversionRate = conversionRate,
//                    invested = invested,
//                    units = units,
//                    open = open,
//                    current = current,
//                    stopLoss = sl,
//                    takeProfit = tp,
//                    profitLoss = pl,
//                    profitLossPercent = plPercent,
//                    pipChange = pipChange,
//                    leverage = leverage,
//                    value = value,
//                    fees = fees,
//                    amountStopLoss = amountSl,
//                    amountTakeProfit = amountTp,
//                    stopLossPercent = slPercent,
//                    takeProfitPercent = tpPercent
//                )
//            )
        }
        return itemList
    }

    private suspend fun calculateAmount(
        instrumentId: Int,
        rate: Float,
        open: Float,
        unit: Float,
        isBuy: Boolean,
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
