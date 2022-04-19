package com.awonar.android.shared.repos

import com.awonar.android.model.feed.FeedPaging
import com.awonar.android.model.feed.FeedResponse
import com.awonar.android.model.feed.NewsMeta
import com.awonar.android.model.feed.NewsMetaResponse
import com.awonar.android.shared.api.FeedService
import com.molysulfur.library.network.DirectNetworkFlow
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeedRepository @Inject constructor(
    val service: FeedService,
) {

    fun getMetaNews(url: String) =
        object : DirectNetworkFlow<Int, NewsMeta?, NewsMetaResponse?>() {
            override fun createCall(): Response<NewsMetaResponse?> =
                service.getMetaNews(url = url).execute()

            override fun convertToResultType(response: NewsMetaResponse?): NewsMeta? =
                response?.meta

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }

        }.asFlow()


    fun getAllFeed(page: Int) =
        object : DirectNetworkFlow<Int, FeedPaging, FeedResponse>() {
            override fun createCall(): Response<FeedResponse> =
                service.getAllFeed(page = page).execute()

            override fun convertToResultType(response: FeedResponse): FeedPaging {
                return FeedPaging(
                    feeds = response.feeds ?: emptyList(),
                    page = if (response.meta?.hasMore == true) response.meta?.page?.plus(1) ?: 0 else 0
                )
            }

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }

        }.asFlow()

}