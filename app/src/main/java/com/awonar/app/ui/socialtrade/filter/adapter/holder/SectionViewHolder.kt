package com.awonar.app.ui.socialtrade.filter.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemSectionBinding
import com.awonar.app.ui.socialtrade.filter.adapter.SocialTradeFilterItem

class SectionViewHolder constructor(private val binding: AwonarItemSectionBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SocialTradeFilterItem.SectionItem) {
        binding.text = item.text
    }
}