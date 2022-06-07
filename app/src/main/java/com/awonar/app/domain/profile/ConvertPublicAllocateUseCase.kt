package com.awonar.app.domain.profile

import com.awonar.android.model.profile.PublicAllocate
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.app.ui.portfolio.adapter.PortfolioItem
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ConvertPublicAllocateUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<List<PublicAllocate>, MutableList<PortfolioItem>>(dispatcher) {
    override suspend fun execute(parameters: List<PublicAllocate>): MutableList<PortfolioItem> {
        val itemList = mutableListOf<PortfolioItem>()
        val colors = ColorTemplate.MATERIAL_COLORS.toMutableList()
        itemList.add(PortfolioItem.TitleItem("Allocate"))
        itemList.add(PortfolioItem.SubTitleItem("Click on the pie chart or legend item to drill down"))
        val entries = arrayListOf<PieEntry>()
        parameters.forEach { publicExposure ->
            entries.add(PieEntry(publicExposure.value, publicExposure.name ?: ""))
        }
        itemList.add(PortfolioItem.PieChartItem(entries))
        parameters.forEachIndexed { index, publicExposure ->
            itemList.add(
                PortfolioItem.ListTextItem(
                    publicExposure.name, "%.2f%s".format(publicExposure.value, "%"),
                    colors[index % colors.size]
                )
            )
        }
        itemList.add(PortfolioItem.ButtonItem("Exposure"))
        return itemList
    }
}