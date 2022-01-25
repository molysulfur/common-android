package com.awonar.app.ui.socialtrade.filter.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemDescriptionBinding
import com.awonar.app.ui.socialtrade.filter.adapter.SocialTradeFilterItem


class DescriptionViewHolder constructor(private val binding: AwonarItemDescriptionBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: SocialTradeFilterItem.DescriptionItem) {
        binding.text = item.text
    }
}