package com.awonar.app.domain.profile

import android.os.Parcelable
import com.awonar.android.model.user.StatGainResponse
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.app.ui.profile.stat.StatisticItem
import com.awonar.app.utils.DateUtils
import com.github.mikephil.charting.data.Entry
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.parcelize.Parcelize
import timber.log.Timber
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

@Parcelize
data class ConvertGrowthRequest(
    val stat: StatGainResponse,
    val year: Int = 0,
    val statDay: Map<String, Float>,
    val isShowMore: Boolean = false,
    val isShowGrowth: Boolean = false,
) : Parcelable

class ConvertGrowthStatisticToItemUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<ConvertGrowthRequest, MutableList<StatisticItem>>(dispatcher) {
    override suspend fun execute(parameters: ConvertGrowthRequest): MutableList<StatisticItem> {
        val stat = parameters.stat
        val thisYear = parameters.year
        val itemList = mutableListOf<StatisticItem>()
        itemList.add(StatisticItem.ButtonGroupItem(
            buttonText1 = "Chart",
            buttonText2 = "Growth",
            isGrowth = parameters.isShowGrowth
        ))
        val dates = stat.years.map { DateUtils.getDate(it.date, "yyyy") }
        itemList.add(StatisticItem.SelectorItem(thisYear, dates))
        if (parameters.isShowGrowth) {
            var counting = 0f
            val entries = parameters.statDay.map {
                Entry(counting++, it.value)
            }
            itemList.add(StatisticItem.GrowthDayItem(entries))
        } else {
            convertGrowthMonthChartItem(stat, itemList, thisYear)
        }

        val yearGain =
            stat.years.find { DateUtils.getDate(it.date, "yyyy").toInt() == thisYear }
        itemList.add(StatisticItem.TotalGainItem("Total", yearGain?.gain?.times(100) ?: 0f))

        if (parameters.isShowMore) {
            stat.months.forEach { gain ->
                val gainYear = DateUtils.getDate(gain.date, "YYYY").toInt()
                if (gainYear == thisYear) {
                    val monthName = DateUtils.getDate(gain.date, "MMMM")
                    itemList.add(StatisticItem.TotalGainItem(monthName,
                        gain.gain.times(100)))
                }
            }
            itemList.add(StatisticItem.ButtonItem("View Less"))
        } else {
            val currentGain = stat.months.findLast { gain ->
                val gainYear = DateUtils.getDate(gain.date, "YYYY").toInt()
                gainYear == thisYear
            }
            val monthName = DateUtils.getDate(currentGain?.date, "MMMM")
            itemList.add(StatisticItem.TotalGainItem(monthName,
                currentGain?.gain?.times(100) ?: 0f))
            itemList.add(StatisticItem.ButtonItem("View More"))
        }

        return itemList
    }

    private fun convertGrowthMonthChartItem(
        stat: StatGainResponse,
        itemList: MutableList<StatisticItem>,
        thisYear: Int,
    ) {
        val entries = MutableList(12) { index ->
            Entry(index.toFloat(), 0f)
        }
        stat.months
            .filter { DateUtils.getDate(it.date, "yyyy").toInt() == thisYear }
            .forEach { statGain ->
                val index: Int = DateUtils.getDate(statGain.date, "MM").toInt() - 1
                if (index < 12)
                    entries[index] = Entry(index.toFloat(), statGain.gain.times(100))
            }
        itemList.add(StatisticItem.PositiveNegativeChartItem(entries))
    }
}