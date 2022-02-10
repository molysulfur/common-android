package com.awonar.app.domain.profile

import android.os.Parcelable
import com.awonar.android.model.user.DrawdownResponse
import com.awonar.android.model.user.StatRiskResponse
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.app.ui.profile.stat.StatisticItem
import com.github.mikephil.charting.data.BarEntry
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

class ConvertRiskStatisticToItemUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<StatRiskItemRequest, MutableList<StatisticItem>>(dispatcher) {
    override suspend fun execute(parameters: StatRiskItemRequest): MutableList<StatisticItem> {
        val risks = parameters.stat?.risks
        val drawdown = parameters.drawdown
        val itemList = mutableListOf<StatisticItem>()
        itemList.add(StatisticItem.RiskItem(1))
        val entries = MutableList(12) { index ->
            BarEntry(index.toFloat(), 0f)
        }
        risks?.forEachIndexed { index, statRisk ->
            entries[index] = BarEntry(index.toFloat(),
                0f,
                floatArrayOf(statRisk.risk.toFloat(), statRisk.maxRisk.toFloat()))
        }
        itemList.add(StatisticItem.StackedChartItem(entries))
        itemList.add(StatisticItem.TextBoxItem(
            title = "%.2f".format(drawdown?.dailyDrawdown?.times(100)),
            subTitle = "Daliy"))
        itemList.add(StatisticItem.TextBoxItem(
            title = "%.2f".format(drawdown?.weeklyDrawdown?.times(100)),
            subTitle = "Weekly"
        ))
        itemList.add(StatisticItem.TextBoxItem(
            title = "%.2f".format(drawdown?.yearlyDrawdown?.times(100)),
            subTitle = "Yearly"
        ))
        return itemList
    }

}


@Parcelize
data class StatRiskItemRequest(
    val stat: StatRiskResponse?,
    val drawdown: DrawdownResponse?,
) : Parcelable