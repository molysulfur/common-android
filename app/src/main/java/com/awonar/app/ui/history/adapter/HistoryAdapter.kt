package com.awonar.app.ui.history.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.awonar.android.model.history.History
import com.awonar.app.databinding.AwonarItemCashflowCollapsibleBinding
import com.awonar.app.databinding.AwonarItemDividerBinding
import com.awonar.app.databinding.AwonarItemHistoryBinding
import com.awonar.app.databinding.AwonarItemLoadingBinding
import com.awonar.app.ui.history.adapter.holder.CashFlowViewHolder
import com.awonar.app.ui.history.adapter.holder.DividerViewHolder
import com.awonar.app.ui.history.adapter.holder.HistoryViewHolder
import com.awonar.app.ui.history.adapter.holder.LoadMoreViewHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class HistoryAdapter:
    RecyclerView.Adapter<ViewHolder>() {

    private val dispatcher = CoroutineScope(Dispatchers.Default)

    var itemLists: MutableList<HistoryItem> = mutableListOf()
        set(value) {
            val old = field
            field = value
            DiffUtil.calculateDiff(HistoryDiffUtil(old, value))
                .dispatchUpdatesTo(this)
        }

    var columns: List<String> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onClick: ((History) -> Unit)? = null
    var onLoad: ((Int) -> Unit)? = null
    var onShowInsideInstrument: ((String, String?) -> Unit)? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemLists[position]
        when (holder) {
            is HistoryViewHolder -> item.let {
                holder.bind(
                    item as HistoryItem.PositionItem,
                    columns,
                    onClick,
                    onShowInsideInstrument
                )
            }
            is LoadMoreViewHolder -> {
                onLoad?.invoke((item as HistoryItem.LoadMoreItem).page)
            }
            is CashFlowViewHolder -> {
                holder.bind(
                    item as HistoryItem.CashFlowItem
                )
            }
        }

    }

    override fun getItemViewType(position: Int): Int = itemLists[position].type

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        when (viewType) {
            HistoryType.POSITION_HISTORY -> HistoryViewHolder(
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
            HistoryType.CASHFLOW_HISTORY -> CashFlowViewHolder(
                AwonarItemCashflowCollapsibleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            HistoryType.DIVIDER_HISTORY -> DividerViewHolder(
                AwonarItemDividerBinding.inflate(
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
        private val newItems: List<HistoryItem>?,
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