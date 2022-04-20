package com.awonar.app.domain.profile

import com.awonar.android.model.profile.PublicExposure
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.app.ui.portfolio.chart.adapter.PositionChartItem
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ConvertPublicExposureUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<List<PublicExposure>, MutableList<PositionChartItem>>(dispatcher) {
    override suspend fun execute(parameters: List<PublicExposure>): MutableList<PositionChartItem> {
        val itemList = mutableListOf<PositionChartItem>()
        val colors = ColorTemplate.MATERIAL_COLORS.toMutableList()
        itemList.add(PositionChartItem.TitleItem("Exposure"))
        itemList.add(PositionChartItem.SubTitleItem("Click on the pie chart or legend item to drill down"))
        val entries = arrayListOf<PieEntry>()
        parameters.forEach { publicExposure ->
            entries.add(PieEntry(publicExposure.value, publicExposure.name ?: ""))
        }
        itemList.add(PositionChartItem.PieChartItem(entries))
        parameters.forEachIndexed { index, publicExposure ->
            itemList.add(
                PositionChartItem.ListItem(
                    publicExposure.name, "%.2f%s".format(publicExposure.value, "%"),
                    colors[index % colors.size]
                )
            )
        }
        itemList.add(PositionChartItem.ButtonItem("Allocate"))
        return itemList
    }
}