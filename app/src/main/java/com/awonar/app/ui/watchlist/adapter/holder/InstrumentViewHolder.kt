package com.awonar.app.ui.watchlist.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemInstrumentListBinding
import com.awonar.app.ui.watchlist.adapter.WatchlistItem
import kotlinx.coroutines.Dispatchers
import okhttp3.Dispatcher

class InstrumentViewHolder constructor(private val binding: AwonarItemInstrumentListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val instrumentId: Int = 0

    fun bind(item: WatchlistItem.InstrumentItem) {
        with(binding.awonarInstrumentItemList) {
            setImage(item.image ?: "")
            setTitle(item.title ?: "")
        }
    }

}