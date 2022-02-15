package com.awonar.app.domain.profile

import com.awonar.android.model.user.StatTradeResponse
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.app.ui.profile.stat.StatisticItem
import com.github.mikephil.charting.data.PieEntry
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import timber.log.Timber
import javax.inject.Inject

class ConvertMostTradingStatisticToItemUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
) :
    UseCase<StatTradeResponse?, MutableList<StatisticItem>>(dispatcher) {
    override suspend fun execute(parameters: StatTradeResponse?): MutableList<StatisticItem> {
        Timber.e("$parameters")
        val itemList = mutableListOf<StatisticItem>()
        var otherPercentRadio = 0f
        val investmentSorted = parameters?.investmentStats?.sortedByDescending { it.tradesRatio }
        val mostTradeList = mutableListOf<StatisticItem.ListItem>()
        for (index in 0..7) {
            val intrumentTrade = investmentSorted?.get(index)
            val radio = intrumentTrade?.tradesRatio ?: 0f
            otherPercentRadio += radio
            mostTradeList.add(StatisticItem.ListItem(
                text = intrumentTrade?.symbol,
                number = radio
            ))
        }
        mostTradeList.add(StatisticItem.ListItem(
            text = "Other",
            number = 100.minus(otherPercentRadio)
        ))

        val pieChartItem = mostTradeList.map {
            PieEntry(it.number)
        }
        itemList.add(StatisticItem.PieChartItem(pieChartItem))
        itemList.addAll(mostTradeList)
        itemList.add(StatisticItem.DividerItem())

        return itemList
    }
}