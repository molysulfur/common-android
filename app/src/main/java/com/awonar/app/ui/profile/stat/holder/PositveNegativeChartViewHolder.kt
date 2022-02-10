package com.awonar.app.ui.profile.stat.holder

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemBarchartBinding
import com.awonar.app.ui.profile.stat.StatisticItem
import com.awonar.app.widget.BarRechartWebView
import com.awonar.app.widget.StackedRechartWebView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.molysulfur.library.utils.ColorUtils
import java.time.Month

class PositveNegativeChartViewHolder constructor(private val binding: AwonarItemBarchartBinding) :
    RecyclerView.ViewHolder(binding.root) {


    private val formatter = object : IndexAxisValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return super.getFormattedValue(value)
        }
    }

    @SuppressLint("JavascriptInterface", "SetJavaScriptEnabled")
    fun bind(item: StatisticItem.PositiveNegativeChartItem) {
        val entities = item.entries.mapIndexed { index, entry ->
            BarRechartWebView.BarRechartEntity(
                label = Month.values()[index].toString(),
                value = entry.y,
                index = index
            )
        }
        with(binding.awonarItemBarchart) {
            setListener(object : BarRechartWebView.IBarRechartListener {
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