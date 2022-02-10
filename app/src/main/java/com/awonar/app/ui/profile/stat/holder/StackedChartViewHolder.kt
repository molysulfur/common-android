package com.awonar.app.ui.profile.stat.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemStackedChartBinding
import com.awonar.app.ui.profile.stat.StatisticItem
import com.awonar.app.widget.StackedRechartWebView
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import timber.log.Timber
import java.time.Month

class StackedChartViewHolder constructor(private val binding: AwonarItemStackedChartBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val formatter = object : IndexAxisValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return super.getFormattedValue(value)
        }
    }

    fun bind(item: StatisticItem.StackedChartItem) {
        val entities: List<StackedRechartWebView.StackedRechartEntity> =
            item.entries.mapIndexed { index, entry ->
                if (entry.data != null) {
                    StackedRechartWebView.StackedRechartEntity(
                        label = Month.values()[index].toString(),
                        avg = (entry.data as FloatArray)[0],
                        max = (entry.data as FloatArray)[1]
                    )
                } else {
                    StackedRechartWebView.StackedRechartEntity(
                        label = Month.values()[index].toString(),
                        avg = 0f,
                        max = 0f
                    )
                }
            }
        with(binding.awonarItemStackedchart) {
            setListener(object : StackedRechartWebView.IStackedRechartListener {
                override fun chartAlready() {
                    post {
                        setData(entities)
                    }
                }
            })
            start()
        }
    }
}