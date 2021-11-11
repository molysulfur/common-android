package com.awonar.app.domain.portfolio

import com.awonar.android.shared.di.IoDispatcher
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioItem
import com.github.mikephil.charting.data.PieEntry
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject


class ConvertAllocateToPieChartUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<Map<String, Double>, List<OrderPortfolioItem>>(dispatcher) {
    override suspend fun execute(parameters: Map<String, Double>): List<OrderPortfolioItem> {
        val itemList = mutableListOf<OrderPortfolioItem>()
        itemList.add(OrderPortfolioItem.TitleItem("Allocate"))
        itemList.add(OrderPortfolioItem.SubTitleItem("Click on the pie chart or legend item to drill down"))
        val entries = arrayListOf<PieEntry>()
        for ((k, v) in parameters) {
            entries.add(PieEntry(v.toFloat(), k))
        }
        itemList.add(OrderPortfolioItem.PieChartItem(entries))
        for ((k, v) in parameters) {
            itemList.add(
                OrderPortfolioItem.ListItem(
                    k, v.toFloat()
                )
            )
        }
        itemList.add(OrderPortfolioItem.ButtonItem("View All"))
        itemList.add(OrderPortfolioItem.ButtonItem("Exposure"))
        return itemList
    }

}