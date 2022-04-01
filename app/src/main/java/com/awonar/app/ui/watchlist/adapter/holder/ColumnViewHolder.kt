package com.awonar.app.ui.watchlist.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemWatchlistColumnsBinding
import com.awonar.app.ui.watchlist.adapter.WatchlistItem

class ColumnViewHolder constructor(private val binding: AwonarItemWatchlistColumnsBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: WatchlistItem.ColumnItem) {
        binding.column1 = item.column1
        binding.column2 = item.column2
        binding.column3 = item.column3
    }
}