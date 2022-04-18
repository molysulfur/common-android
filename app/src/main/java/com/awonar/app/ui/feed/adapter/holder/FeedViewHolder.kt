package com.awonar.app.ui.feed.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemDefaultFeedBinding
import com.awonar.app.ui.feed.adapter.FeedItem

class FeedViewHolder constructor(private val binding: AwonarItemDefaultFeedBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: FeedItem.DefaultFeed) {
        with(binding.awonarDefaultFeedItem) {
            avatar = item.avatar
            title = item.title
            subTitle = item.subTitle
            description = item.description
        }
    }
}