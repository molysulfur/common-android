package com.awonar.app.ui.marketprofile.about.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemTitleBinding
import com.awonar.app.ui.marketprofile.about.MarketAboutItem

class TitleViewHolder constructor(private val binding: AwonarItemTitleBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: MarketAboutItem.TitleItem) {
        binding.text = item.title
    }
}