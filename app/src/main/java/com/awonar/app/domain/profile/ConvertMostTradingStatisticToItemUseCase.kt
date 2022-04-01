package com.awonar.app.domain.profile

import com.awonar.android.model.user.StatTradeResponse
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.app.ui.profile.stat.StatisticItem
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import timber.log.Timber
import javax.inject.Inject

class ConvertMostTradingStatisticToItemUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
) :
    UseCase<StatTradeResponse?, MutableList<StatisticItem>>(dispatcher) {

    companion object {
        private const val MAX_SIZE_MOST_ITEM = 8
    }

    override suspend fun execute(parameters: StatTradeResponse?): MutableList<StatisticItem> {
        val itemList = mutableListOf<StatisticItem>()
        var otherPercentRadio = 0f
        val investmentSorted = parameters?.investmentStats?.sortedByDescending { it.tradesRatio }
        val colors = ColorTemplate.MATERIAL_COLORS.toMutableList()

        val mostTradeList = mutableListOf<StatisticItem.ListItem>()
        for (index in 0 until MAX_SIZE_MOST_ITEM) {
            val intrumentTrade = investmentSorted?.get(index)
            val radio = intrumentTrade?.tradesRatio ?: 0f
            otherPercentRadio += radio
            mostTradeList.add(StatisticItem.ListItem(
                text = intrumentTrade?.symbol,
                number = radio,
                color = colors[index % colors.size]
            ))
        }
        mostTradeList.add(StatisticItem.ListItem(
            text = "Other",
            number = 100.minus(otherPercentRadio),
            color = colors[MAX_SIZE_MOST_ITEM % colors.size]
        ))
        val pieChartItem = mostTradeList.map {
            PieEntry(it.number)
        }
        itemList.add(StatisticItem.TitleItem(
            "Most Trading"
        ))
        itemList.add(StatisticItem.PieChartItem(pieChartItem))
        itemList.addAll(mostTradeList)
        itemList.add(StatisticItem.DividerItem())
        itemList.add(StatisticItem.SectionItem(
            "Trading"
        ))
        itemList.add(
            StatisticItem.TotalTradeItem(
                total = parameters?.tradeTotal ?: 0,
                profitAvg = parameters?.avgProfit ?: 0f,
                lossAvg = parameters?.avgLoss ?: 0f,
            ))
        val sortPerformance = parameters?.performanceTrades?.sortedByDescending { it.ratio }
        val weights = sortPerformance?.map {
            it.ratio
        } ?: arrayListOf()
        itemList.add(StatisticItem.LinearColorsItem(weights))
        sortPerformance?.forEachIndexed { index, statPerformanceTrade ->
            itemList.add(StatisticItem.ListItem(
                text = statPerformanceTrade.category,
                number = statPerformanceTrade.ratio,
                color = colors[index % colors.size]
            ))
        }
        return itemList
    }
}