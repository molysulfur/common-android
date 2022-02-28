package com.awonar.app.domain.profile

import com.awonar.android.model.portfolio.HistoryPosition
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.app.ui.profile.history.adapter.HistoryProfileItem
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ConvertHistoryPositionToItemUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<List<HistoryPosition>, MutableList<HistoryProfileItem>>(dispatcher) {
    override suspend fun execute(parameters: List<HistoryPosition>): MutableList<HistoryProfileItem> {
        val itemList = mutableListOf<HistoryProfileItem>()
        parameters.forEach {
            itemList.add(HistoryProfileItem.PositionItem(
                it.instrument?.logo,
                it.instrument?.symbol,
                "",
                it.totalPositions,
                it.profitabilityPercentage,
                it.netProfitPercentage
            ))
        }
        return itemList
    }
}