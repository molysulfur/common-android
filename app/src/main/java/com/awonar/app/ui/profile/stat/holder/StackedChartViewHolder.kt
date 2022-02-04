package com.awonar.app.ui.profile.stat.holder

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemBarchartBinding
import com.awonar.app.ui.profile.stat.StatisticItem
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.molysulfur.library.utils.ColorUtils

class StackedChartViewHolder constructor(private val binding: AwonarItemBarchartBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val formatter = object : IndexAxisValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return super.getFormattedValue(value)
        }
    }

    fun bind(item: StatisticItem.StackedChartItem) {
        with(binding.awonarItemBarchart) {
            description.isEnabled = false
            setDrawValueAboveBar(true)
            val xAxis: XAxis = xAxis
            xAxis.setDrawGridLines(false)
            xAxis.setDrawAxisLine(false)
            xAxis.textSize = 12f
            xAxis.granularity = 1f
            xAxis.valueFormatter = formatter
            val values = mutableListOf<BarEntry>()
            val colors = mutableListOf<Int>()
            val green: Int = Color.rgb(43, 172, 64)
            val red: Int = Color.rgb(255, 1, 0)
            for (i in 0 until item.entries.size) {
                val d: BarEntry = item.entries[i]
                values.add(d)
                if (d.y >= 0) colors.add(green) else colors.add(red)
                colors.add(0)
            }
            val barDataSet = BarDataSet(values, "Values")
            barDataSet.colors = colors
            barDataSet.barBorderWidth = 1f
            setDrawGridBackground(false)
            data = BarData(barDataSet).apply {
            }
            invalidate()
        }
    }
}