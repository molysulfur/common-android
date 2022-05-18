package com.awonar.app.domain.portfolio

import com.awonar.android.model.order.Price
import com.awonar.android.model.order.StopLossRequest
import com.awonar.android.model.portfolio.Position
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.domain.order.CalculateAmountStopLossAndTakeProfitWithBuyUseCase
import com.awonar.android.shared.domain.order.CalculateAmountStopLossAndTakeProfitWithSellUseCase
import com.awonar.android.shared.repos.CurrenciesRepository
import com.awonar.app.ui.portfolio.adapter.PortfolioItem
import com.awonar.app.utils.DateUtils
import com.molysulfur.library.result.data
import com.molysulfur.library.result.succeeded
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject


class ConvertPositionToItemUseCase @Inject constructor(
    private val currenciesRepository: CurrenciesRepository,
    private val calculateAmountStopLossAndTakeProfitWithBuyUseCase: CalculateAmountStopLossAndTakeProfitWithBuyUseCase,
    private val calculateAmountStopLossAndTakeProfitWithSellUseCase: CalculateAmountStopLossAndTakeProfitWithSellUseCase,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<List<Position>, List<PortfolioItem>>(dispatcher) {
    override suspend fun execute(parameters: List<Position>): MutableList<PortfolioItem> {
        val itemList = mutableListOf<PortfolioItem>()
        parameters.forEachIndexed { index, position ->
            val conversionRate =
                currenciesRepository.getConversionByInstrumentId(position.instrumentId).rateBid
            val invested = position.amount
            val units = position.units
            val open = position.openRate
            val leverage = position.leverage
            val fees = position.totalFees
            val sl = position.stopLossRate
            val tp = position.takeProfitRate
            val amountSl = calculateAmount(position.instrumentId, sl, open, units, position.isBuy)
            val amountTp = calculateAmount(position.instrumentId, tp, open, units, position.isBuy)
            val slPercent = amountSl.times(100).div(invested)
            val tpPercent = amountTp.times(100).div(invested)
            val date =
                if (position.exitOrder != null) "Pending" else DateUtils.getDate(position.openDateTime)
            itemList.add(
                PortfolioItem.InstrumentPortfolioItem(
                    position = position,
                    conversionRate = conversionRate,
                    meta = date,
                    index = index,
                    isRealTime = true
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
