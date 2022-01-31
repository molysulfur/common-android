package com.awonar.app.ui.watchlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.*
import com.awonar.app.ui.watchlist.adapter.holder.*

class WatchlistAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var itemList: MutableList<WatchlistItem> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    var onToggleWatchlistInstrument: ((Int, Boolean) -> Unit)? = null
    var onToggleWatchlistTrader: ((String?, Boolean) -> Unit)? = null
    var onButtonClick: ((String?) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            WatchlistType.WATCHLIST_ITEM_SELECTOR -> ListSelectorViewHolder(
                AwonarItemListBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            )
            WatchlistType.WATCHLIST_COLUMNS -> ColumnViewHolder(
                AwonarItemWatchlistColumnsBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            )
            WatchlistType.WATCHLIST_INSTRUMENT -> InstrumentViewHolder(
                AwonarItemInstrumentListBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            )
            WatchlistType.WATCHLIST_TRADER -> TraderViewHolder(
                AwonarItemCopiesItemBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            )
            WatchlistType.WATCHLIST_BUTTON -> ButtonViewHolder(
                AwonarItemButtonViewmoreBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            )
            WatchlistType.WATCHLIST_EMPTY -> EmptyViewHolder(
                AwonarItemEmptyBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            )
            else -> throw Error("View type is not found!.")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemList[position]
        when (holder) {
            is ListSelectorViewHolder -> holder.bind(item as WatchlistItem.SelectorItem,
                onToggleWatchlistInstrument, onToggleWatchlistTrader)
            is ColumnViewHolder -> holder.bind(item as WatchlistItem.ColumnItem)
            is InstrumentViewHolder -> holder.bind(item as WatchlistItem.InstrumentItem)
            is TraderViewHolder -> holder.bind(item as WatchlistItem.TraderItem)
            is ButtonViewHolder -> holder.bind(item as WatchlistItem.ButtonItem, onButtonClick)
            is EmptyViewHolder -> holder.bind(item as WatchlistItem.EmptyItem)
        }
    }

    override fun getItemViewType(position: Int): Int = itemList[position].type

    override fun getItemCount(): Int = itemList.size
}