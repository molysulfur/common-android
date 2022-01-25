package com.awonar.app.ui.socialtrade.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemCopiesItemBinding
import com.awonar.app.databinding.AwonarItemLoadingBinding
import com.awonar.app.databinding.AwonarItemTitleViewmoreBinding
import com.awonar.app.ui.history.adapter.holder.LoadMoreViewHolder
import com.awonar.app.ui.socialtrade.adapter.holder.CopiesItemViewHolder
import com.awonar.app.ui.socialtrade.adapter.holder.TitleViewHolder
import com.awonar.app.ui.socialtrade.filter.adapter.SocialTradeFilterItem

class SocialTradeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var itemList: MutableList<SocialTradeItem> = mutableListOf()
        set(value) {
            val old = field
            field = value
            DiffUtil.calculateDiff(SocialTradeDiffCallback(old, value)).dispatchUpdatesTo(this)
        }

    var onItemClick: ((String?) -> Unit)? = null
    var onWatchListClick: (() -> Unit)? = null
    var onLoadMore: (() -> Unit?)? = null
    var onViewMore: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            SocialTradeType.SOCIALTRADE_TITLE -> TitleViewHolder(
                AwonarItemTitleViewmoreBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            SocialTradeType.SOCIALTRADE_COPIES_ITEM -> CopiesItemViewHolder(
                AwonarItemCopiesItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            SocialTradeType.SOCIALTRADE_LOADMORE -> LoadMoreViewHolder(
                AwonarItemLoadingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw Error("View Type is not found!")
        }

    override fun getItemViewType(position: Int): Int = itemList[position].type

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemList[position]
        when (holder) {
            is TitleViewHolder -> holder.bind(item as SocialTradeItem.TitleItem, onViewMore)
            is CopiesItemViewHolder -> holder.bind(
                item as SocialTradeItem.CopiesItem,
                onItemClick,
                onWatchListClick
            )
            is LoadMoreViewHolder -> {
                onLoadMore?.invoke()
            }
        }
    }

    override fun getItemCount(): Int = itemList.size

    private class SocialTradeDiffCallback(
        private val oldItems: MutableList<SocialTradeItem>,
        private val newItems: MutableList<SocialTradeItem>,
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems[oldItemPosition] === newItems[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems[oldItemPosition] == newItems[newItemPosition]
        }
    }
}