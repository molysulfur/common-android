package com.awonar.app.ui.marketprofile.stat.overview.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemInfoBinding
import com.awonar.app.ui.marketprofile.stat.overview.OverviewMarketItem

class InfoViewHolder constructor(private val binding: AwonarItemInfoBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(marketItem: OverviewMarketItem.InfoMarketItem) {
        with(binding.awonarItemInfo) {
            setTitle(marketItem.title ?: "")
            setDescription(marketItem.description ?: "")
        }
    }
}