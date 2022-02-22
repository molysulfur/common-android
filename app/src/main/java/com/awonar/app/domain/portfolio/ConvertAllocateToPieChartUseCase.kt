package com.awonar.app.domain.portfolio

import com.awonar.android.model.portfolio.PieChartRequest
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.app.ui.portfolio.adapter.PortfolioItem
import com.github.mikephil.charting.data.PieEntry
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ConvertAllocateToPieChartUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<PieChartRequest, List<PortfolioItem>>(dispatcher) {
    override suspend fun execute(parameters: PieChartRequest): List<PortfolioItem> {
        val itemList = mutableListOf<PortfolioItem>()
        itemList.add(PortfolioItem.TitleItem("Allocate"))
        itemList.add(PortfolioItem.SubTitleItem("Click on the pie chart or legend item to drill down"))
        val entries = arrayListOf<PieEntry>()
        for ((k, v) in parameters.data) {
            entries.add(PieEntry(v.toFloat(), k))
        }
        itemList.add(PortfolioItem.PieChartItem(entries))
        for ((k, v) in parameters.data) {
            itemList.add(
                PortfolioItem.ListItem(
                    k, v.toFloat()
                )
            )
        }
        if (parameters.hasViewAll)
            itemList.add(PortfolioItem.ViewAllItem("View All"))
        itemList.add(PortfolioItem.ButtonItem("Exposure"))
        return itemList
    }

}
