package com.awonar.app.ui.portfolio.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.R
import com.awonar.app.databinding.AwonarItemEmptyBinding
import com.awonar.app.databinding.AwonarItemListBinding
import com.awonar.app.databinding.AwonarItemListPiechartBinding
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioItem

class ListItemViewHolder constructor(
    private val binding: AwonarItemListPiechartBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: OrderPortfolioItem.ListItem) {
        binding.label = item.name
        binding.value = "%.2f%s".format(item.value, "%")
    }
}