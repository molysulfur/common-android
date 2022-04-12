package com.awonar.android.shared.repos

import com.awonar.android.model.feed.FeedPaging
import com.awonar.android.model.feed.FeedResponse
import com.awonar.android.shared.api.FeedService
import com.molysulfur.library.network.DirectNetworkFlow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeedRepository @Inject constructor(
    val service: FeedService,
) {


    fun getAllFeed(page: Int) =
        object : DirectNetworkFlow<Int, FeedPaging, FeedResponse>() {
            override fun createCall(): Response<FeedResponse> =
                service.getAllFeed(page = page).execute()

            override fun convertToResultType(response: FeedResponse): FeedPaging {
                return FeedPaging(
                    feeds = response.feeds ?: emptyList(),
                    page = response.meta?.page ?: 0
                )
            }

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }


        }.asFlow()

}