package com.awonar.app.domain.portfolio

import com.awonar.android.model.portfolio.Copier
import com.awonar.android.model.portfolio.Position
import com.awonar.android.shared.di.MainDispatcher
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioItem
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ConvertCopierToItemUseCase @Inject constructor(
    @MainDispatcher dispatcher: CoroutineDispatcher
) : UseCase<List<Copier>, List<OrderPortfolioItem>>(dispatcher) {
    override suspend fun execute(parameters: List<Copier>): List<OrderPortfolioItem> {
        val itemList = mutableListOf<OrderPortfolioItem>()
        parameters.forEach { copier ->
            val totalUnits: Double = sumUnit(copier.positions)
            val avgOpen: Double = calAvgOpen(copier.positions.filter { it.isBuy },
                copier.positions.filter { !it.isBuy })
            val invested = copier.initialInvestment
            val fees = copier.totalFees
            val csl = copier.stopLossAmount
            val cslPercent = copier.stopLossPercentage
            val netInvested = 0f
            val current = 0f
            val pl = 0f
            val plPercent = 0f
            val value = 0f
            val leverage = 0f
            itemList.add(
                OrderPortfolioItem.CopierPortfolioItem(
                    copier = copier,
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
                    copyStopLossPercent = cslPercent
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
