package com.awonar.app.domain.portfolio

import com.awonar.android.model.portfolio.Position
import com.awonar.android.model.portfolio.UserPortfolioResponse
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.CurrenciesRepository
import com.awonar.app.ui.portfolio.adapter.PortfolioItem
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ConvertManualPositionToItemUseCase @Inject constructor(
    private val currenciesRepository: CurrenciesRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<UserPortfolioResponse, MutableList<PortfolioItem>>(dispatcher) {

    override suspend fun execute(parameters: UserPortfolioResponse): MutableList<PortfolioItem> {
        val positions = parameters.positions ?: emptyList()
        val itemList = mutableListOf<PortfolioItem>()
        convertInstrumentItem(positions, itemList)
        return itemList
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
}