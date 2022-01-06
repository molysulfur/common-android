package com.awonar.app.ui.socialtrade.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemCopiesItemBinding
import com.awonar.app.databinding.AwonarItemTitleViewmoreBinding
import com.awonar.app.ui.socialtrade.adapter.holder.CopiesItemViewHolder
import com.awonar.app.ui.socialtrade.adapter.holder.TitleViewHolder

class SocialTradeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var itemList: MutableList<SocialTradeItem> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onItemClick: ((String?) -> Unit)? = null
    var onWatchListClick: (() -> Unit)? = null

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
            else -> throw Error("View Type is not found!")
        }

    override fun getItemViewType(position: Int): Int = itemList[position].type

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemList[position]
        when (holder) {
            is TitleViewHolder -> holder.bind(item as SocialTradeItem.TitleItem)
            is CopiesItemViewHolder -> holder.bind(
                item as SocialTradeItem.CopiesItem,
                onItemClick,
                onWatchListClick
            )
        }
    }

    override fun getItemCount(): Int = itemList.size
}