package com.awonar.app.ui.socialtrade.filter.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.R
import com.awonar.app.databinding.AwonarItemListBinding
import com.awonar.app.ui.socialtrade.filter.adapter.SocialTradeFilterItem

class SelectorViewHolder constructor(private val binding: AwonarItemListBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: SocialTradeFilterItem.SelectorListItem, onClick: ((Boolean) -> Unit)?) {
        with(binding.awonarItemListText) {
            setTitle(item.text)
            setEndIcon(if (item.isChecked) R.drawable.awonar_ic_checked else 0)
            setOnClickListener {
                onClick?.invoke(!item.isChecked)
            }
        }
    }
}