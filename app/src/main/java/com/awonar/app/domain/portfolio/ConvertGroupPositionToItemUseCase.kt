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
import kotlin.math.abs


class ConvertGroupPositionToItemUseCase @Inject constructor(
    private val currenciesRepository: CurrenciesRepository,
    private val calculateAmountStopLossAndTakeProfitWithBuyUseCase: CalculateAmountStopLossAndTakeProfitWithBuyUseCase,
    private val calculateAmountStopLossAndTakeProfitWithSellUseCase: CalculateAmountStopLossAndTakeProfitWithSellUseCase,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<List<Position>, List<PortfolioItem>>(dispatcher) {
    override suspend fun execute(parameters: List<Position>): MutableList<PortfolioItem> {
        val groupInstrument: Map<Int, List<Position>> = parameters.groupBy { it.instrumentId }
        val itemList = mutableListOf<PortfolioItem>()
        for ((key, positions) in groupInstrument) {
            val conversionRate = currenciesRepository.getConversionByInstrumentId(key).rateBid
            val invested: Double = positions.sumOf { it.amount.toDouble() }
            val units = positions.sumOf { it.units.toDouble() }
            val avgOpen = positions.sumOf { position ->
                if (position.isBuy) {
                    position.units * position.openRate.toDouble()
                } else {
                    -abs(position.units * position.openRate).toDouble()
                }.div(positions.sumOf {
                    if (it.isBuy) {
                        it.units
                    } else {
                        -it.units
                    }.toDouble()
                })
            }
            val leverage = (positions.sumOf {
                val rate = currenciesRepository.getConversionByInstrumentId(key).rateBid
                it.openRate.times(it.units).times(rate).toDouble()
            }).div(invested)
            val fees = positions.sumOf { it.totalFees.toDouble() }
            val current = 0f // after get realtime
            val pl = 0f // cal after get realtime
            val plPercent = 0f // cal after get realtime
            val pipChange = 0f // cal after get realtime
            val value = 0f// cal after get realtime
            val sl = 0f
            val tp = 0f
            val amountSl = 0f
            val amountTp = 0f
            val slPercent = 0f
            val tpPercent = 0f
            val date = DateUtils.getDate(positions[0].openDateTime)
            itemList.add(
                PortfolioItem.InstrumentPortfolioItem(
                    position = positions[0],
                    /*   conversionRate = conversionRate,
                       invested = invested.toFloat(),
                       units = units.toFloat(),
                       open = avgOpen.toFloat(),
                       current = current,
                       stopLoss = sl,
                       takeProfit = tp,
                       profitLoss = pl,
                       profitLossPercent = plPercent,
                       pipChange = pipChange,
                       leverage = leverage.toInt(),
                       value = value,
                       fees = fees.toFloat(),
                       amountStopLoss = amountSl,
                       amountTakeProfit = amountTp,
                       stopLossPercent = slPercent,
                       takeProfitPercent = tpPercent,*/
                    meta = date,
                    index = parameters.indexOfFirst { it.instrument?.id == positions[0].instrument?.id }
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

    private fun calAvgOpen(buyPositions: List<Position>, sellPosition: List<Position>): Double {
        val sumBuyOpen = buyPositions.sumOf {
            (it.units * it.openRate).toDouble()
        }
        val sumSellOpen = buyPositions.sumOf {
            (it.units * it.openRate).toDouble()
        }
        val sumBuyUnit = buyPositions.sumOf { it.units.toDouble() }
        val sumSellUnit = sellPosition.sumOf { it.units.toDouble() }

        return (sumBuyOpen.minus(sumSellOpen)).div((sumBuyUnit - sumSellUnit))
    }
}

