package com.awonar.app.ui.profile.stat.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemCenterTitleBinding
import com.awonar.app.databinding.AwonarItemSectionBinding
import com.awonar.app.ui.profile.stat.StatisticItem

class TitleViewHolder constructor(private val binding: AwonarItemCenterTitleBinding) :
    RecyclerView.ViewHolder(binding.root) {


    fun bind(item : StatisticItem.TitleItem) {
        binding.title = item.text
    }
}