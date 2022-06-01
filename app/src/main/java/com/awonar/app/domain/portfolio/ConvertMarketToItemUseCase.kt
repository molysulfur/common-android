package com.awonar.app.domain.portfolio

import com.awonar.android.model.portfolio.ConvertMarketRequest
import com.awonar.android.model.portfolio.Position
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.CurrenciesRepository
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
) : UseCase<ConvertMarketRequest, MutableList<PortfolioItem>>(dispatcher) {
    override suspend fun execute(parameters: ConvertMarketRequest): MutableList<PortfolioItem> {
        val positions = parameters.portfolio.positions ?: emptyList()
        val copiers = parameters.portfolio.copies ?: emptyList()
        val groupInstrument: Map<Int, List<Position>> = positions.groupBy { it.instrumentId }
        val itemList = mutableListOf<PortfolioItem>()
        for ((key, positions) in groupInstrument) {
            val conversionRate = currenciesRepository.getConversionByInstrumentId(key).rateBid
            val instrumentId = positions[0].instrument?.id
            val totalUnit = positions.sumOf { it.units.toDouble() }
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
//            val sumPLPercent = sumPL.div(invested) * 100f
            val fees = positions.sumOf { it.totalFees.toDouble() }
            itemList.add(
                PortfolioItem.PositionItem(
                    positionId = positions.indexOfFirst { it.instrument?.id == positions[0].instrument?.id },
                    copierId = null,
                    instrumentGroup = positions,
                    conversionRate = conversionRate
                )
            )
        }
        return itemList
    }
}

