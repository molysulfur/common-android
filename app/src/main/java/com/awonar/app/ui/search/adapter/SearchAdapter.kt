package com.awonar.app.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemSearchItemBinding
import com.awonar.app.ui.search.adapter.holder.SearchItemViewHolder

class SearchAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var itemList: MutableList<SearchItem> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onTradeClick: ((String?) -> Unit)? = null
    var onFollowClick: ((Boolean) -> Unit)? = null
    var onItemClick: ((String?, Boolean) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            SearchType.ITEM -> SearchItemViewHolder(AwonarItemSearchItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ))
            else -> throw Error("View type is not found!")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemList[position]
        when (holder) {
            is SearchItemViewHolder -> holder.bind(item as SearchItem.ListItem,
                onTradeClick,
                onFollowClick,
                onItemClick)
        }
    }

    override fun getItemCount(): Int = itemList.size

    override fun getItemViewType(position: Int): Int {
        return itemList[position].type
    }
}