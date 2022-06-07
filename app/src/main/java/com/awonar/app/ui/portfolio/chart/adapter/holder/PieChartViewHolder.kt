package com.awonar.app.ui.portfolio.chart.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemPiechartBinding
import com.awonar.app.ui.portfolio.adapter.PortfolioItem
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate

class PieChartViewHolder constructor(private val binding: AwonarItemPiechartBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: PortfolioItem.PieChartItem, onPieChartClick: ((String?) -> Unit)?) {
        val dataSet = PieDataSet(item.entries, "Chart Position")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toMutableList()
        binding.awonrItemPiechart.apply {
            setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onValueSelected(e: Entry?, h: Highlight?) {
                    if (e != null) {
                        if (binding.awonrItemPiechart.tag == (e as? PieEntry)?.label) {
                            onPieChartClick?.invoke(binding.awonrItemPiechart.tag.toString())
                            binding.awonrItemPiechart.centerText = ""
                            binding.awonrItemPiechart.tag = ""
                        }
                        binding.awonrItemPiechart.tag = (e as? PieEntry)?.label
                        binding.awonrItemPiechart.apply {
                            centerText =
                                "%.2f%s %s".format(
                                    (e as? PieEntry)?.value,
                                    "%",
                                    (e as? PieEntry)?.label
                                )
                        }
                    }
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