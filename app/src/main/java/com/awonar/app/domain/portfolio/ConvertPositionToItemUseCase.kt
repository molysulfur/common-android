package com.awonar.app.domain.portfolio

import com.awonar.android.model.portfolio.UserPortfolioResponse
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.app.ui.portfolio.adapter.PortfolioItem
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ConvertPositionToItemUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<ConvertInsidePosition, List<PortfolioItem>>(dispatcher) {
    override suspend fun execute(parameters: ConvertInsidePosition): MutableList<PortfolioItem> {
        val itemList = mutableListOf<PortfolioItem>()
        val conversionRate = parameters.currentItem.conversionRate
        val positions = parameters.portfolio.positions
        val instrumentId = parameters.currentItem.instrumentGroup?.get(0)?.instrument?.id
        positions?.filter { it.instrumentId == instrumentId }?.forEach {
            itemList.add(
                PortfolioItem.PositionItem(
                    positionId = it.instrumentId,
                    instrumentGroup = arrayListOf(it),
                    conversionRate = conversionRate
                )
            )
        }
        return itemList
    }
}

data class ConvertInsidePosition(
    val portfolio: UserPortfolioResponse,
    val currentItem: PortfolioItem.PositionItem,
)
