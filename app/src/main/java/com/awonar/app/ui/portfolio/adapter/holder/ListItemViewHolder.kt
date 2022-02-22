package com.awonar.app.ui.portfolio.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemListPiechartBinding
import com.awonar.app.ui.portfolio.adapter.PortfolioItem

class ListItemViewHolder constructor(
    private val binding: AwonarItemListPiechartBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: PortfolioItem.ListItem) {
        binding.label = item.name
        binding.value = "%.2f%s".format(item.value, "%")
    }
}