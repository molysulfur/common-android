package com.awonar.app.ui.profile.stat.holder

import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.R
import com.awonar.app.databinding.AwonarItemLinearViewBinding
import com.awonar.app.ui.profile.stat.StatisticItem
import com.github.mikephil.charting.utils.ColorTemplate
import timber.log.Timber

class HorizontalColorsViewHolder constructor(private val binding: AwonarItemLinearViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: StatisticItem.LinearColorsItem) {
        val colors = ColorTemplate.MATERIAL_COLORS.toMutableList()
        with(binding.awonarItemLinearContainer) {
            val layoutWidth = width
            orientation = LinearLayoutCompat.HORIZONTAL
            item.weightList.forEachIndexed { index, weight ->
                addView(View(binding.root.context).apply {
                    setBackgroundColor(colors[index % colors.size])
                    val param = LinearLayoutCompat.LayoutParams(
                        weight.times(layoutWidth).div(100).toInt(),
                        LinearLayoutCompat.LayoutParams.MATCH_PARENT
                    )
                    layoutParams = param
                })
            }
        }
    }
}