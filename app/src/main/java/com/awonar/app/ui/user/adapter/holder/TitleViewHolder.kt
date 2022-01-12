package com.awonar.app.ui.user.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemTitleBinding
import com.awonar.app.ui.user.adapter.UserInfoItem
import javax.inject.Inject

class TitleViewHolder @Inject constructor(private val binding: AwonarItemTitleBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: UserInfoItem.TitleItem) {
        binding.text = item.title
    }
}