package com.awonar.app.domain.portfolio

import com.awonar.android.model.portfolio.PieChartRequest
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.app.ui.portfolio.adapter.PortfolioItem
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ConvertExposureToPieChartUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<PieChartRequest, List<PortfolioItem>>(dispatcher) {
    override suspend fun execute(parameters: PieChartRequest): List<PortfolioItem> {
        val itemList = mutableListOf<PortfolioItem>()
        val colors = ColorTemplate.MATERIAL_COLORS.toMutableList()
        var index = 0
        itemList.add(PortfolioItem.TitleItem("Exposure"))
        itemList.add(PortfolioItem.SubTitleItem("Click on the pie chart or legend item to drill down"))
        val entries = arrayListOf<PieEntry>()
        for ((k, v) in parameters.data) {
            entries.add(PieEntry(v.toFloat(), k))
        }
        itemList.add(PortfolioItem.PieChartItem(entries))
        for ((k, v) in parameters.data) {
            itemList.add(
                PortfolioItem.ListTextItem(
                    k, "%.2f%s".format(v.toFloat(), "%"), colors[index++ % colors.size]
                )
            )
        }
        if (parameters.hasViewAll)
            itemList.add(PortfolioItem.ButtonItem("View All"))
        itemList.add(PortfolioItem.ButtonItem("Allocate"))
        return itemList
    }

}