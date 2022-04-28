package com.awonar.app.ui.feed.adapter

import android.os.Parcelable
import com.awonar.android.model.feed.Feed
import com.awonar.android.model.feed.NewsMeta
import kotlinx.parcelize.Parcelize

sealed class FeedItem(val type: Int) : Parcelable {

    @Parcelize
    class  EmptyItem : FeedItem(FeedType.EMPTY_TYPE)

    @Parcelize
    class BlankItem : FeedItem(FeedType.BLANK_TYPE)

    @Parcelize
    class LoadingItem : FeedItem(FeedType.LOADING_TYPE)

    @Parcelize
    class ImagesFeeds(
        val avatar: String?,
        val title: String?,
        val subTitle: String?,
        val description: String?,
        val likeCount: Int = 0,
        val sharedCount: Int = 0,
        val commentCount: Int = 0,
        val images: List<String?>,
    ) : FeedItem(FeedType.IMAGES_TYPE)

    @Parcelize
    class DefaultFeed(
        val avatar: String?,
        val title: String?,
        val subTitle: String?,
        val description: String?,
        val sharedFeed: Feed?,
        val likeCount: Int = 0,
        val sharedCount: Int = 0,
        val commentCount: Int = 0,
    ) : FeedItem(FeedType.FEED_TYPE)

    @Parcelize
    class NewsFeed(
        val avatar: String?,
        val title: String?,
        val subTitle: String?,
        val description: String?,
        val newsMeta: NewsMeta?,
        val likeCount: Int = 0,
        val sharedCount: Int = 0,
        val commentCount: Int = 0,
    ) : FeedItem(FeedType.NEWS_TYPE)
}