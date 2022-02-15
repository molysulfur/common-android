package com.awonar.app.ui.profile.stat.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemPiechartBinding
import com.awonar.app.ui.profile.stat.StatisticItem
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate

class PiechartViewHolder constructor(private val binding: AwonarItemPiechartBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: StatisticItem.PieChartItem) {
        val dataSet = PieDataSet(item.entries, "Most Trading")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toMutableList()
        binding.awonrItemPiechart.apply {
            setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onValueSelected(e: Entry?, h: Highlight?) {

                }

                override fun onNothingSelected() {
                }

            })
            isRotationEnabled = false
            legend.isEnabled = false
            setUsePercentValues(true)
            setDrawEntryLabels(false)
            description.isEnabled = false
            data = PieData(dataSet)
            invalidate()
        }

    }
}