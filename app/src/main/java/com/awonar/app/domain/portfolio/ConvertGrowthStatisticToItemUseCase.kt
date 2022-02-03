package com.awonar.app.domain.portfolio

import com.awonar.android.model.user.StatGainResponse
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.di.MainDispatcher
import com.awonar.app.ui.profile.stat.StatisticItem
import com.awonar.app.utils.DateUtils
import com.github.mikephil.charting.data.Entry
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
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
        val dates = parameters.years.map { DateUtils.getDate(it.date, "yyyy") }
        itemList.add(StatisticItem.SelectorItem(dates))

        val entries = MutableList(12) { index ->
            Entry(index.toFloat(), 0f)
        }
        val thisYear = Calendar.getInstance().get(Calendar.YEAR)
        parameters.months
            .filter { DateUtils.getDate(it.date, "yyyy").toInt() == thisYear }
            .forEachIndexed { index, statGain ->
                entries[index] = Entry(index.toFloat(), statGain.gain.times(100))
            }
        itemList.add(StatisticItem.PositiveNegativeChartItem(entries))
        return itemList
    }
}