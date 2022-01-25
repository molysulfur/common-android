package com.awonar.app.ui.user.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemSectionBinding
import com.awonar.app.ui.user.adapter.UserInfoItem
import javax.inject.Inject

class SectionViewHolder @Inject constructor(private val binding: AwonarItemSectionBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: UserInfoItem.SubTitleItem) {
        binding.text = item.subTitle
    }
}