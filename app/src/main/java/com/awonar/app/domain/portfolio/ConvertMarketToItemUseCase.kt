package com.awonar.app.domain.portfolio

import com.awonar.android.model.portfolio.ConvertMarketRequest
import com.awonar.android.model.portfolio.Copier
import com.awonar.android.model.portfolio.Position
import com.awonar.android.model.portfolio.UserPortfolioResponse
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.CurrenciesRepository
import com.awonar.android.shared.repos.PortfolioRepository
import com.awonar.android.shared.utils.ConverterQuoteUtil
import com.awonar.android.shared.utils.PortfolioUtil
import com.awonar.app.ui.portfolio.adapter.PortfolioItem
import com.awonar.app.utils.DateUtils
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.abs


class ConvertMarketToItemUseCase @Inject constructor(
    private val currenciesRepository: CurrenciesRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<UserPortfolioResponse, MutableList<PortfolioItem>>(dispatcher) {
    override suspend fun execute(parameters: UserPortfolioResponse): MutableList<PortfolioItem> {
        val positions = parameters.positions ?: emptyList()
        val copiers = parameters.copies ?: emptyList()
        val itemList = mutableListOf<PortfolioItem>()
        convertInstrumentItem(positions, itemList)
        convertCopierItem(copiers, itemList)
        return itemList
    }

    private fun convertCopierItem(
        copiers: List<Copier>,
        itemList: MutableList<PortfolioItem>
    ) {
        copiers.forEachIndexed { index, copier ->
            val conversions = HashMap<Int, Float>()
            copier.positions?.forEach { position ->
                val conversion =
                    currenciesRepository.getConversionByInstrumentId(position.instrumentId)
                conversions[position.instrumentId] = conversion.rateBid
            }
            val moneyInMoneyOut = copier.depositSummary - copier.withdrawalSummary
            val totalUnits: Double = copier.positions?.sumOf {
                it.units.toDouble()
            } ?: 0.0
            val avgOpen: Double =
                calAvgOpenCopy(copier.positions?.filter { it.isBuy } ?: emptyList(),
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
            with(copier) {
                this.units = totalUnits.toFloat()
                this.avgOpen = avgOpen.toFloat()
                this.invested = invested
                this.profitLoss = pl
                this.profitLossPercent = plPercent
                this.value = value
                this.fees = fees
                this.leverage = leverage
                this.current = current
                this.netInvested = netInvested
                this.copyStopLoss = csl
                this.copyStopLossPercent = cslPercent
            }
            itemList.add(
                PortfolioItem.CopierPortfolioItem(
                    copier = copier,
                    conversions = conversions,
                    index = index
                )
            )
        }

    }

    private fun convertInstrumentItem(
        positions: List<Position>,
        itemList: MutableList<PortfolioItem>
    ) {
        val groupInstrument: Map<Int, List<Position>> = positions.groupBy { it.instrumentId }
        for ((key, positions) in groupInstrument) {
            val conversionRate = currenciesRepository.getConversionByInstrumentId(key).rateBid
            itemList.add(
                PortfolioItem.PositionItem(
                    positionId = positions.indexOfFirst { it.instrument?.id == positions[0].instrument?.id },
                    instrumentGroup = positions,
                    conversionRate = conversionRate
                )
            )
        }
    }

    private fun calAvgOpenCopy(buyPositions: List<Position>, sellPosition: List<Position>): Double {
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

