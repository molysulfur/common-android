package com.awonar.app.ui.feed.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.awonar.android.model.feed.NewsMeta
import com.awonar.app.databinding.AwonarItemDefaultFeedBinding
import com.awonar.app.ui.feed.adapter.FeedItem
import com.awonar.app.widget.feed.FeedCardView
import com.awonar.app.widget.feed.PreviewFeed
import timber.log.Timber

class NewsFeedViewHolder constructor(private val binding: AwonarItemDefaultFeedBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: FeedItem.NewsFeed) {
        with(binding.awonarDefaultFeedItem) {
            avatar = item.avatar
            title = item.title
            subTitle = item.subTitle
            description = item.description
            likeCount = item.likeCount
            commentCount = item.commentCount
            sharedCount = item.sharedCount
            if (item.newsMeta != null) {
                addOptionView(createFeedView(item.newsMeta))
            } else {
                clearOptionView()
            }

        }
    }

    private fun createFeedView(news: NewsMeta?): View {
        return FeedCardView(context = binding.root.context).apply {
            image = news?.image
            title = news?.title
            meta = "%s . %s".format(news?.siteName, news?.hostname)
            description = news?.description
        }
    }
}