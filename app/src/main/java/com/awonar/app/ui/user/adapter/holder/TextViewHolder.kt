package com.awonar.app.ui.user.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemListBinding
import com.awonar.app.databinding.AwonarItemTitleBinding
import com.awonar.app.ui.user.adapter.UserInfoItem
import javax.inject.Inject

class TextViewHolder @Inject constructor(private val binding: AwonarItemListBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: UserInfoItem.TextItem) {
        binding.text = item.text
    }
}