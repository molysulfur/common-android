package com.awonar.app.domain.portfolio

import com.awonar.android.model.portfolio.PieChartRequest
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.app.ui.portfolio.chart.adapter.PositionChartItem
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import timber.log.Timber
import javax.inject.Inject

class ConvertAllocateToPieChartUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<PieChartRequest, List<PositionChartItem>>(dispatcher) {
    override suspend fun execute(parameters: PieChartRequest): List<PositionChartItem> {
        val itemList = mutableListOf<PositionChartItem>()
        val colors = ColorTemplate.MATERIAL_COLORS.toMutableList()
        var index = 0
        itemList.add(PositionChartItem.TitleItem("Allocate"))
        itemList.add(PositionChartItem.SubTitleItem("Click on the pie chart or legend item to drill down"))
        val entries = arrayListOf<PieEntry>()
        for ((k, v) in parameters.data) {
            Timber.e("$k $v")
            entries.add(PieEntry(v.toFloat(), k))
        }
        itemList.add(PositionChartItem.PieChartItem(entries))
        for ((k, v) in parameters.data) {
            itemList.add(
                PositionChartItem.ListItem(
                    k, "%.2f%s".format(v.toFloat(), "%"),
                    colors[index++ % colors.size]
                )
            )

        }
        if (parameters.hasViewAll)
            itemList.add(PositionChartItem.ButtonItem("View All"))
        itemList.add(PositionChartItem.ButtonItem("Exposure"))
        return itemList
    }

}
