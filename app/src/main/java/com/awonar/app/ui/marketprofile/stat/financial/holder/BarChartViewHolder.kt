package com.awonar.app.ui.marketprofile.stat.financial.holder

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemMpBarchartBinding
import com.awonar.app.ui.marketprofile.stat.financial.FinancialMarketItem
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import timber.log.Timber


class BarChartViewHolder constructor(private val binding: AwonarItemMpBarchartBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: FinancialMarketItem.BarChartItem) {
        with(binding.awonarMpBarchartItem) {
            if (data != null && data.dataSetCount > 0) {
                item.entries.forEachIndexed { index, barEntryItem ->
                    val set1 = data.getDataSetByIndex(index) as BarDataSet
                    set1.values = barEntryItem.entries
                }
                data.notifyDataChanged()
                notifyDataSetChanged()
            } else {
                val sets = mutableListOf<BarDataSet>()
                val colors = ColorTemplate.MATERIAL_COLORS.toMutableList()
                item.entries.forEachIndexed { index, barEntryItem ->
                    val set1 = BarDataSet(barEntryItem.entries, barEntryItem.title)
                    set1.color = colors[index % colors.size]
                    sets.add(set1)
                }
                val barData = BarData(sets.toList())
                data = barData
            }
            setTouchEnabled(false)
            invalidate()
        }
    }
}