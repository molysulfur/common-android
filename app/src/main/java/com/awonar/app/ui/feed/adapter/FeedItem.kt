package com.awonar.app.ui.feed.adapter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class FeedItem(val type: Int) : Parcelable {

    @Parcelize
    class DefaultFeed(
        val avatar: String?,
        val title: String?,
        val subTitle: String?,
        val description: String?,
        val likeCount: Int = 0,
        val sharedCount: Int = 0,
        val commentCount: Int = 0,
    ) : FeedItem(FeedType.FEED_TYPE)
}