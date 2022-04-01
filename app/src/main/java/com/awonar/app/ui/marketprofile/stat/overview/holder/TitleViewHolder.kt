package com.awonar.app.ui.marketprofile.stat.overview.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemTitleBinding
import com.awonar.app.ui.marketprofile.stat.overview.OverviewMarketItem

class TitleViewHolder constructor(private val binding: AwonarItemTitleBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(marketItem: OverviewMarketItem.TitleMarketItem) {
        binding.text = marketItem.title
    }
}