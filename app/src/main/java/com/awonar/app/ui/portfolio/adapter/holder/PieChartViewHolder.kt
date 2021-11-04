package com.awonar.app.ui.portfolio.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemPiechartBinding
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioItem
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate

class PieChartViewHolder constructor(private val binding: AwonarItemPiechartBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val listener = object : OnChartValueSelectedListener {
        override fun onValueSelected(e: Entry?, h: Highlight?) {
            if (e != null) {
                binding.awonrItemPiechart.apply {
                    centerText =
                        "$%.2f %s}".format((e as? PieEntry)?.value, (e as? PieEntry)?.label)
                }
            }
        }

        override fun onNothingSelected() {
        }

    }

    fun bind(item: OrderPortfolioItem.PieChartItem) {
        val dataSet = PieDataSet(item.entries, "Exposure")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toMutableList()
        binding.awonrItemPiechart.apply {
            setOnChartValueSelectedListener(listener)
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