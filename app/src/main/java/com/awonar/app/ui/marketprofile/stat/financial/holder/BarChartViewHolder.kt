package com.awonar.app.ui.marketprofile.stat.financial.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemMpBarchartBinding
import com.awonar.app.ui.marketprofile.stat.financial.FinancialMarketItem
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.utils.ColorTemplate


class BarChartViewHolder constructor(private val binding: AwonarItemMpBarchartBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: FinancialMarketItem.BarChartItem) {
        with(binding.awonarMpBarchartItem) {
            setTouchEnabled(false)
            description.isEnabled = false
            setDrawGridBackground(false)
            val xAxis: XAxis = xAxis
            xAxis.granularity = 1f
            axisRight.isEnabled = false
            xAxis.axisMinimum = 0f
            xAxis.setCenterAxisLabels(true)
            val sets = mutableListOf<BarDataSet>()
            val colors = ColorTemplate.MATERIAL_COLORS.toMutableList()
            item.entries.forEachIndexed { index, barEntryItem ->
                val set1 = BarDataSet(barEntryItem.entries, barEntryItem.title)
                set1.color = colors[index % colors.size]
                sets.add(set1)
            }
            val barData = BarData(sets.toList())
            barData.barWidth = 0.2f
            data = barData
            if (item.entries.size > 1) {
                groupBars(0f, 0.1f, 0f)
            }
            data.notifyDataChanged()
            notifyDataSetChanged()
//            invalidate()
        }
    }
}