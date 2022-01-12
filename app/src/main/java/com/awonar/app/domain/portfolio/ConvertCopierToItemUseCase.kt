package com.awonar.app.domain.portfolio

import com.awonar.android.model.portfolio.Copier
import com.awonar.android.model.portfolio.Position
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.CurrenciesRepository
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioItem
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import timber.log.Timber
import javax.inject.Inject

class ConvertCopierToItemUseCase @Inject constructor(
    private val currenciesRepository: CurrenciesRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<List<Copier>, MutableList<OrderPortfolioItem>>(dispatcher) {
    override suspend fun execute(parameters: List<Copier>): MutableList<OrderPortfolioItem> {
        val itemList = mutableListOf<OrderPortfolioItem>()
        parameters.forEachIndexed { index, copier ->
            val conversions = HashMap<Int, Float>()
            copier.positions?.forEach { position ->
                val conversion =
                    currenciesRepository.getConversionByInstrumentId(position.instrumentId)
                conversions[position.instrumentId] = conversion.rateBid
            }
            val moneyInMoneyOut = copier.depositSummary - copier.withdrawalSummary
            val totalUnits: Double = sumUnit(copier.positions ?: emptyList())
            val avgOpen: Double = calAvgOpen(copier.positions?.filter { it.isBuy } ?: emptyList(),
                copier.positions?.filter { !it.isBuy } ?: emptyList())
            val fees = copier.totalFees
            val csl = copier.stopLossAmount
            val cslPercent = copier.stopLossPercentage
            val invested = copier.initialInvestment.plus(moneyInMoneyOut)
            val netInvested = copier.initialInvestment.plus(moneyInMoneyOut)
            val current = 0f // not used
            val leverage = 0f  // not used
            val pl = 0f // cal after get realtime
            val plPercent = 0f// cal after get realtime
            val value = 0f // cal after get realtime
            itemList.add(
                OrderPortfolioItem.CopierPortfolioItem(
                    copier = copier,
                    conversions = conversions,
                    units = totalUnits.toFloat(),
                    avgOpen = avgOpen.toFloat(),
                    invested = invested,
                    profitLoss = pl,
                    profitLossPercent = plPercent,
                    value = value,
                    fees = fees,
                    leverage = leverage,
                    current = current,
                    netInvested = netInvested,
                    copyStopLoss = csl,
                    copyStopLossPercent = cslPercent,
                    index = index
                )
            )
        }

        return itemList
    }

    private fun sumUnit(positions: List<Position>): Double = positions.sumOf {
        it.units.toDouble()
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
