package com.awonar.app.ui.socialtrade.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemTitleBinding
import com.awonar.app.ui.socialtrade.adapter.SocialTradeItem

class TitleViewHolder constructor(
    private val binding: AwonarItemTitleBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SocialTradeItem.TitleItem) {
        binding.text = item.title
    }
}