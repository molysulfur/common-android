package com.awonar.app.domain.feed

import com.awonar.android.model.feed.Feed
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.app.ui.feed.adapter.FeedItem
import com.awonar.app.utils.DateUtils
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ConvertFeedsToItemsUseCase @Inject constructor(@IoDispatcher dispatcher: CoroutineDispatcher) :
    UseCase<ConvertFeedData, MutableList<FeedItem>>(dispatcher) {
    override suspend fun execute(parameters: ConvertFeedData): MutableList<FeedItem> {
        val itemList = mutableListOf<FeedItem>()
        parameters.feeds.forEach {
            itemList.add(FeedItem.DefaultFeed(
                avatar = it?.user?.picture,
                title = it?.user?.username,
                subTitle = DateUtils.getTimeAgo(it?.createdAt),
                description = it?.description,
                sharedFeed = it?.sharePostData
            ))
            itemList.add(FeedItem.BlankItem())
        }
        if (parameters.page > 0) {
            itemList.add(FeedItem.LoadingItem())
        }
        return itemList
    }
}

data class ConvertFeedData(
    val feeds: MutableList<Feed?>,
    val page: Int,
)