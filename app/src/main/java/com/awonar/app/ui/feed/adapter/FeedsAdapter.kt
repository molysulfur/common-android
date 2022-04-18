package com.awonar.app.ui.feed.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemDefaultFeedBinding
import com.awonar.app.ui.feed.adapter.holder.FeedViewHolder

class FeedsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var itemLists: MutableList<FeedItem> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            FeedType.FEED_TYPE -> FeedViewHolder(AwonarItemDefaultFeedBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ))
            else -> throw Error("View type is not found!")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemLists[position]
        when(holder){
            is FeedViewHolder -> holder.bind(item as FeedItem.DefaultFeed)
        }
    }

    override fun getItemCount(): Int = itemLists.size

    override fun getItemViewType(position: Int): Int = itemLists[position].type
}