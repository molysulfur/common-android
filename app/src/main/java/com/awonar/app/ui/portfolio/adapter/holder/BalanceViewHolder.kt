package com.awonar.app.ui.portfolio.adapter.holder

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.R
import com.awonar.app.databinding.AwonarItemListBinding
import com.awonar.app.ui.portfolio.adapter.PortfolioItem

class BalanceViewHolder constructor(private val binding: AwonarItemListBinding) :
    RecyclerView.ViewHolder(binding.root) {


    fun bind(item: PortfolioItem.BalanceItem) {
        binding.awonarItemListText.setBackgroundColor(ContextCompat.getColor(binding.root.context,
            R.color.awonar_color_light_gray))
        binding.awonarItemListText.setTitle(item.title ?: "")
        binding.awonarItemListText.setMeta("%.2f%s".format(item.value, "%"))
    }
}