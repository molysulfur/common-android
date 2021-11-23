package com.awonar.app.domain.portfolio

import com.awonar.android.model.portfolio.PieChartRequest
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioItem
import com.github.mikephil.charting.data.PieEntry
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ConvertExposureToPieChartUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<PieChartRequest, List<OrderPortfolioItem>>(dispatcher) {
    override suspend fun execute(parameters: PieChartRequest): List<OrderPortfolioItem> {
        val itemList = mutableListOf<OrderPortfolioItem>()
        itemList.add(OrderPortfolioItem.TitleItem("Exposure"))
        itemList.add(OrderPortfolioItem.SubTitleItem("Click on the pie chart or legend item to drill down"))
        val entries = arrayListOf<PieEntry>()
        for ((k, v) in parameters.data) {
            entries.add(PieEntry(v.toFloat(), k))
        }
        itemList.add(OrderPortfolioItem.PieChartItem(entries))
        for ((k, v) in parameters.data) {
            itemList.add(
                OrderPortfolioItem.ListItem(
                    k, v.toFloat()
                )
            )
        }
        if (parameters.hasViewAll)
            itemList.add(OrderPortfolioItem.ViewAllItem("View All"))
        itemList.add(OrderPortfolioItem.ButtonItem("Allocate"))
        return itemList
    }

}