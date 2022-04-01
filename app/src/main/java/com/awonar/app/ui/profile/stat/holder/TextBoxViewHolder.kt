package com.awonar.app.ui.profile.stat.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemTextBoxBinding
import com.awonar.app.ui.profile.stat.StatisticItem

class TextBoxViewHolder constructor(private val binding: AwonarItemTextBoxBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: StatisticItem.TextBoxItem) {
        binding.title = item.title
        binding.subTitle = item.subTitle
    }

}