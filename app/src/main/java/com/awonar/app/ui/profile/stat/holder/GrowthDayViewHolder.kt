package com.awonar.app.ui.profile.stat.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemLineChartBinding
import com.awonar.app.ui.profile.stat.StatisticItem
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import timber.log.Timber

class GrowthDayViewHolder constructor(private val binding: AwonarItemLineChartBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: StatisticItem.GrowthDayItem) {
        with(binding.awonarItemLineChart) {
            axisRight.isEnabled = false
            xAxis.isEnabled = false
            description.text = ""
            legend.isEnabled = false
            val set1 = LineDataSet(item.entries, "Days Growth")
            with(set1) {
                mode = LineDataSet.Mode.CUBIC_BEZIER;
                cubicIntensity = 0.2f
                circleRadius = 4f
                setDrawCircles(false)
                lineWidth = 1.8f
            }
            val dataSets = arrayListOf<ILineDataSet>()
            dataSets.add(set1)
            val data = LineData(dataSets)
            setData(data)
            invalidate()
        }
    }
}