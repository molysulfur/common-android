package com.awonar.app.ui.portfolio.adapter.holder

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemListPiechartBinding
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioItem
import com.github.mikephil.charting.utils.ColorTemplate

class ListItemViewHolder constructor(
    private val binding: AwonarItemListPiechartBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: OrderPortfolioItem.ListItem, position: Int) {
        val color = ColorTemplate.MATERIAL_COLORS.toMutableList()
        binding.label = item.name
        binding.awonarItemListPiechartTitle.setTextColor(
            ContextCompat.getColor(
                binding.root.context,
                color[position]
            )
        )
        binding.value = "%.2f%s".format(item.value, "%")
    }
}