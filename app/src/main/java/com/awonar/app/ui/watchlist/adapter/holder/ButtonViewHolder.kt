package com.awonar.app.ui.watchlist.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemButtonViewmoreBinding
import com.awonar.app.ui.watchlist.adapter.WatchlistItem

class ButtonViewHolder constructor(private val binding: AwonarItemButtonViewmoreBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(buttonItem: WatchlistItem.ButtonItem) {
        binding.buttonText = buttonItem.buttonText
    }
}