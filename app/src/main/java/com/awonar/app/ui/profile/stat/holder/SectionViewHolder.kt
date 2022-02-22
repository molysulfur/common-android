package com.awonar.app.ui.profile.stat.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemSectionBinding
import com.awonar.app.ui.profile.stat.StatisticItem

class SectionViewHolder constructor(private val binding: AwonarItemSectionBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: StatisticItem.SectionItem) {
        binding.text = item.text
    }
}