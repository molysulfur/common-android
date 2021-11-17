package com.awonar.app.ui.history.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.awonar.android.model.history.History
import com.awonar.app.databinding.AwonarItemHistoryBinding
import com.awonar.app.databinding.AwonarItemLoadingBinding
import com.awonar.app.ui.history.adapter.holder.HistoryViewHolder
import com.awonar.app.ui.history.adapter.holder.LoadMoreViewHolder
import com.awonar.app.ui.history.adapter.holder.MarketViewHolder

class HistoryAdapter : RecyclerView.Adapter<ViewHolder>() {

    var itemLists: MutableList<HistoryItem> = mutableListOf()
        set(value) {
            val old = field
            field = value
            DiffUtil.calculateDiff(HistoryDiffUtil(old, value))
                .dispatchUpdatesTo(this)
        }

    @SuppressLint("NotifyDataSetChanged")
    var columns: List<String> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onClick: ((History) -> Unit)? = null
    var onLoad: ((Int) -> Unit)? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemLists[position]
        when (holder) {
            is HistoryViewHolder -> item?.let {
                holder.bind(item as HistoryItem.ManualItem, columns, onClick)
            }
            is LoadMoreViewHolder -> {
                onLoad?.invoke((item as HistoryItem.LoadMoreItem).page)
            }
        }

    }

    override fun getItemViewType(position: Int): Int = itemLists[position].type

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        when (viewType) {
            HistoryType.MANUAL_HISTORY -> HistoryViewHolder(
                AwonarItemHistoryBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            HistoryType.MARKET_HISTORY -> MarketViewHolder(
                AwonarItemHistoryBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            HistoryType.LOADMORE_HISTORY -> LoadMoreViewHolder(
                AwonarItemLoadingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw Error("View Type is not found with type $viewType")
        }

    override fun getItemCount(): Int = itemLists.size

    class HistoryDiffUtil(
        private val oldItems: List<HistoryItem>?,
        private val newItems: List<HistoryItem>?
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldItems?.size ?: 0

        override fun getNewListSize(): Int = newItems?.size ?: 0

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems?.getOrNull(oldItemPosition) === newItems?.getOrNull(newItemPosition)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems?.getOrNull(oldItemPosition) == newItems?.getOrNull(newItemPosition)
        }
    }
}