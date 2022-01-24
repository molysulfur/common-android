package com.awonar.app.ui.socialtrade.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemTitleBinding
import com.awonar.app.databinding.AwonarItemTitleViewmoreBinding
import com.awonar.app.ui.socialtrade.adapter.SocialTradeItem

class TitleViewHolder constructor(
    private val binding: AwonarItemTitleViewmoreBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SocialTradeItem.TitleItem, onViewMore: ((String) -> Unit)?) {
        binding.title = item.title
        binding.buttonText = item.buttonText
        binding.awonarTitleButtonViewmore.setOnClickListener {
            onViewMore?.invoke(item.key)
        }
    }
}