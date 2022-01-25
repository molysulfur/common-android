package com.awonar.app.ui.socialtrade.filter.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemListBinding
import com.awonar.app.ui.socialtrade.filter.adapter.SocialTradeFilterItem

class ListTextViewHolder constructor(private val binding: AwonarItemListBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: SocialTradeFilterItem.TextListItem, onClick: ((String?) -> Unit)?) {
        with(binding.awonarItemListText) {
            setTitle(item.text)
            setMeta(item.meta ?: "")
            setOnClickListener {
                onClick?.invoke(item.key)
            }
        }
    }
}