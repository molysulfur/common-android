package com.awonar.app.ui.watchlist.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.R
import com.awonar.app.databinding.AwonarItemListBinding
import com.awonar.app.ui.watchlist.adapter.WatchlistItem

class ListSelectorViewHolder constructor(private val binding: AwonarItemListBinding) :
    RecyclerView.ViewHolder(binding.root) {


    fun bind(
        selectorItem: WatchlistItem.SelectorItem,
        onToggleWatchlistInstrument: ((Int, Boolean) -> Unit)?,
        onToggleWatchlistTrader: ((String?, Boolean) -> Unit)?,
    ) {
        with(binding.awonarItemListText) {
            setStartIcon(selectorItem.image ?: "")
            setTitle(selectorItem.title ?: "")
            setEndIcon(if (selectorItem.isSelected) R.drawable.awonar_ic_checked else 0)
            setOnClickListener {
                if (selectorItem.instrumentId > 0) {
                    onToggleWatchlistInstrument?.invoke(selectorItem.instrumentId,
                        !selectorItem.isSelected)
                } else if (selectorItem.uid != null) {
                    onToggleWatchlistTrader?.invoke(selectorItem.uid, !selectorItem.isSelected)
                }
            }
        }
    }
}