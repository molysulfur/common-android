package com.awonar.app.domain.portfolio

import com.awonar.android.shared.di.IoDispatcher
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioItem
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject


class ConvertExposureToPieChartUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<Map<String, Double>, List<OrderPortfolioItem>>(dispatcher) {
    override suspend fun execute(parameters: Map<String, Double>): List<OrderPortfolioItem> {
        val itemList = mutableListOf<OrderPortfolioItem>()
        for ((k, v) in parameters) {
            itemList.add(
                OrderPortfolioItem.PieChartExposureItem(
                    k, v.toFloat()
                )
            )
        }
        return itemList
    }

}