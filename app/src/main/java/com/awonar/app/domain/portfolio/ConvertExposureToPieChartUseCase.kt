package com.awonar.app.domain.portfolio

import com.awonar.android.model.portfolio.PieChartRequest
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.app.ui.portfolio.adapter.PortfolioItem
import com.awonar.app.ui.portfolio.chart.adapter.PositionChartItem
import com.github.mikephil.charting.data.PieEntry
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ConvertExposureToPieChartUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<PieChartRequest, List<PositionChartItem>>(dispatcher) {
    override suspend fun execute(parameters: PieChartRequest): List<PositionChartItem> {
        val itemList = mutableListOf<PositionChartItem>()
        itemList.add(PositionChartItem.TitleItem("Exposure"))
        itemList.add(PositionChartItem.SubTitleItem("Click on the pie chart or legend item to drill down"))
        val entries = arrayListOf<PieEntry>()
        for ((k, v) in parameters.data) {
            entries.add(PieEntry(v.toFloat(), k))
        }
        itemList.add(PositionChartItem.PieChartItem(entries))
        for ((k, v) in parameters.data) {
            itemList.add(
                PositionChartItem.ListItem(
                    k, "%.2f".format(v.toFloat()), 0
                )
            )
        }
        if (parameters.hasViewAll)
            itemList.add(PositionChartItem.ButtonItem("View All"))
        itemList.add(PositionChartItem.ButtonItem("Allocate"))
        return itemList
    }

}