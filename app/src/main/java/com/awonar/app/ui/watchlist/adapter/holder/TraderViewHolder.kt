package com.awonar.app.ui.watchlist.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemCopiesItemBinding
import com.awonar.app.ui.watchlist.adapter.WatchlistItem
import com.awonar.app.utils.ColorChangingUtil

class TraderViewHolder constructor(private val binding: AwonarItemCopiesItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(traderItem: WatchlistItem.TraderItem) {
        with(binding.awonarItemCopiesItemHolder) {
            image = traderItem.image
            title = traderItem.title
            subTitle = traderItem.subTitle
            risk = traderItem.risk
            gain = traderItem.gain
            gainColor = ColorChangingUtil.getTextColorChange(traderItem.gain)
            hasWatchlistIcon = false
        }
    }

}