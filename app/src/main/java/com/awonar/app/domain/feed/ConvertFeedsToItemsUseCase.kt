package com.awonar.app.domain.feed

import com.awonar.android.model.feed.Feed
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.app.ui.feed.adapter.FeedItem
import com.awonar.app.utils.DateUtils
import com.awonar.app.utils.ImageUtil
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ConvertFeedsToItemsUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
) :
    UseCase<ConvertFeedData, MutableList<FeedItem>>(dispatcher) {
    override suspend fun execute(parameters: ConvertFeedData): MutableList<FeedItem> {
        val itemList = mutableListOf<FeedItem>()
        itemList.add(FeedItem.BlankItem())
        parameters.feeds.forEach {
            when {
                (it?.images?.size ?: 0) > 0 -> itemList.add(
                    FeedItem.ImagesFeeds(
                        avatar = it?.user?.picture,
                        title = it?.user?.username,
                        subTitle = DateUtils.getTimeAgo(it?.createdAt),
                        description = it?.description,
                        commentCount = it?.countComment ?: 0,
                        likeCount = it?.countLike ?: 0,
                        sharedCount = it?.shareTotal ?: 0,
                        images = it?.images ?: listOf()
                    )
                )
                it?.sharePostData != null -> itemList.add(
                    FeedItem.DefaultFeed(
                        avatar = it.user?.picture,
                        title = it.user?.username,
                        subTitle = DateUtils.getTimeAgo(it.createdAt),
                        description = it.description,
                        sharedFeed = it.sharePostData,
                        commentCount = it.countComment,
                        likeCount = it.countLike,
                        sharedCount = it.shareTotal
                    )
                )
                it?.meta != null -> itemList.add(
                    FeedItem.NewsFeed(
                        avatar = it.user?.picture,
                        title = it.user?.username,
                        subTitle = DateUtils.getTimeAgo(it.createdAt),
                        description = it.description,
                        newsMeta = it.meta
                    )
                )
                else -> itemList.add(
                    FeedItem.DefaultFeed(
                        avatar = it?.user?.picture,
                        title = it?.user?.username,
                        subTitle = DateUtils.getTimeAgo(it?.createdAt),
                        description = it?.description,
                        sharedFeed = it?.sharePostData,
                        commentCount = it?.countComment ?: 0,
                        likeCount = it?.countLike ?: 0,
                        sharedCount = it?.shareTotal ?: 0
                    )
                )
            }
            itemList.add(FeedItem.BlankItem())
        }
        if (parameters.page > 0) {
            itemList.add(FeedItem.LoadingItem())
        } else if (itemList.size <= 0) {
            itemList.add(FeedItem.EmptyItem())
        }
        return itemList
    }
}

data class ConvertFeedData(
    val feeds: MutableList<Feed?>,
    val page: Int,
)