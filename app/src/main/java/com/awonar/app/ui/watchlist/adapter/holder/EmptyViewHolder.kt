package com.awonar.app.ui.watchlist.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemEmptyBinding
import com.awonar.app.ui.watchlist.adapter.WatchlistItem
import com.awonar.app.utils.ImageUtil

class EmptyViewHolder constructor(private val binding: AwonarItemEmptyBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(emptyItem: WatchlistItem.EmptyItem) {
        binding.title = emptyItem.title
        binding.subTitle = emptyItem.description
        with(binding.awonarItemEmptyImageLogo) {
            when {
                emptyItem.icon != null -> ImageUtil.loadImage(this, emptyItem.icon)
                emptyItem.iconRes > 0 -> setImageResource(emptyItem.iconRes)
            }

        }
    }
}