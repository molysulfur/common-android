package com.awonar.app.ui.marketprofile.stat.financial.holder

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.R
import com.awonar.app.databinding.AwonarItemMpBarchartBinding
import com.awonar.app.ui.marketprofile.stat.financial.FinancialMarketItem
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.molysulfur.library.utils.ColorUtils


class BarChartViewHolder constructor(private val binding: AwonarItemMpBarchartBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: FinancialMarketItem.BarChartItem) {
        with(binding.awonarMpBarchartItem) {
            setTouchEnabled(false)
            description.isEnabled = false
            setDrawGridBackground(false)
            val xAxis: XAxis = xAxis
            xAxis.granularity = 1f
//            axisRight.isEnabled = false
            axisLeft.isEnabled = false
            xAxis.setCenterAxisLabels(true)
            val sets = mutableListOf<BarDataSet>()
            val colors =
                binding.root.context.resources.getStringArray(R.array.awonar_colors).toMutableList()
            item.entries.forEachIndexed { index, barEntryItem ->
                val set1 = BarDataSet(barEntryItem.entries, barEntryItem.title)
                set1.color = ColorUtils.parseHexColor(colors[index])
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
            invalidate()
        }
    }
}