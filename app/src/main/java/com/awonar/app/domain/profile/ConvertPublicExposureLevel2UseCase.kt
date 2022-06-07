package com.awonar.app.domain.profile

import com.awonar.android.model.profile.PublicExposure
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.app.ui.portfolio.adapter.PortfolioItem
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ConvertPublicExposureLevel2UseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<List<PublicExposure>, MutableList<PortfolioItem>>(dispatcher) {
    override suspend fun execute(parameters: List<PublicExposure>): MutableList<PortfolioItem> {
        val sumValue = parameters.sumOf { it.value.toDouble() }.toFloat()
        val itemList = mutableListOf<PortfolioItem>()
        val colors = ColorTemplate.MATERIAL_COLORS.toMutableList()
        itemList.add(PortfolioItem.TitleItem("Exposure"))
        itemList.add(PortfolioItem.SubTitleItem("Click on the pie chart or legend item to drill down"))
        val entries = arrayListOf<PieEntry>()
        parameters.forEach { publicExposure ->
            val value = publicExposure.value.div(sumValue).times(100)
            entries.add(PieEntry(value,
                publicExposure.name ?: ""))
        }
        itemList.add(PortfolioItem.PieChartItem(entries))
        parameters.forEachIndexed { index, publicExposure ->
            val value = publicExposure.value.div(sumValue).times(100)
            itemList.add(
                PortfolioItem.ListTextItem(
                    publicExposure.name,
                    "%.2f%s".format(value, "%"),
                    colors[index % colors.size]
                )
            )
        }
        itemList.add(PortfolioItem.ButtonItem("Allocate"))
        return itemList
    }
}

