package com.awonar.app.domain.profile

import com.awonar.android.model.user.StatGainResponse
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.app.ui.profile.stat.StatisticItem
import com.awonar.app.utils.DateUtils
import com.github.mikephil.charting.data.Entry
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ConvertGrowthStatisticToItemUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<StatGainResponse, MutableList<StatisticItem>>(dispatcher) {
    override suspend fun execute(parameters: StatGainResponse): MutableList<StatisticItem> {
        val itemList = mutableListOf<StatisticItem>()
        itemList.add(StatisticItem.ButtonGroupItem(
            buttonText1 = "Chart",
            buttonText2 = "Growth"
        ))
        val thisYear = Calendar.getInstance().get(Calendar.YEAR)
        val dates = parameters.years.map { DateUtils.getDate(it.date, "yyyy") }
        itemList.add(StatisticItem.SelectorItem(thisYear, dates))
        val entries = MutableList(12) { index ->
            Entry(index.toFloat(), 0f)
        }
        parameters.months
            .filter { DateUtils.getDate(it.date, "yyyy").toInt() == thisYear }
            .forEachIndexed { index, statGain ->
                entries[index] = Entry(index.toFloat(), statGain.gain.times(100))
            }
        itemList.add(StatisticItem.PositiveNegativeChartItem(entries))
        val yearGain =
            parameters.years.find { DateUtils.getDate(it.date, "yyyy").toInt() == thisYear }
        itemList.add(StatisticItem.TotalGainItem("Total", yearGain?.gain?.times(100) ?: 0f))

        val currentGain = parameters.months.last()
        val monthName = DateUtils.getDate(currentGain.date, "MMMM")
        itemList.add(StatisticItem.TotalGainItem(monthName,
            currentGain.gain.times(100)))
        itemList.add(StatisticItem.ButtonItem("View More"))
        return itemList
    }
}