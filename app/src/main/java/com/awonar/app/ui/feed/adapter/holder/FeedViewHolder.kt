package com.awonar.app.ui.feed.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.feed.SharedFeed
import com.awonar.app.databinding.AwonarItemDefaultFeedBinding
import com.awonar.app.ui.feed.adapter.FeedItem
import com.awonar.app.widget.feed.DefaultFeed
import com.awonar.app.widget.feed.PreviewFeed

class FeedViewHolder constructor(private val binding: AwonarItemDefaultFeedBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: FeedItem.DefaultFeed) {
        with(binding.awonarDefaultFeedItem) {
            avatar = item.avatar
            title = item.title
            subTitle = item.subTitle
            description = item.description
            likeCount = item.likeCount
            commentCount = item.commentCount
            sharedCount = item.sharedCount
            if (item.sharedFeed != null) {
                addOptionView(createFeedView(item.sharedFeed))
            } else {
                clearOptionView()
            }

        }
    }

    private fun createFeedView(sharedFeed: SharedFeed): View {
        return PreviewFeed(context = binding.root.context).apply {
            avatar = sharedFeed.user?.picture
            title = sharedFeed.user?.username
            subTitle = sharedFeed.createdAt
            description = sharedFeed.description
        }
    }
}