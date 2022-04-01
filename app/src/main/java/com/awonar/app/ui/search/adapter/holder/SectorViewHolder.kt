package com.awonar.app.ui.search.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemSectorBinding
import com.awonar.app.ui.search.adapter.SearchItem

class SectorViewHolder constructor(private val binding: AwonarItemSectorBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SearchItem.SectorItem, onClear: (() -> Unit)?) {
        with(binding.awonarSectorItem) {
            text = item.text
            textAction = item.action
            onClick = {
                onClear?.invoke()
            }
        }
    }
}