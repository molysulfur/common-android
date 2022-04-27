package com.awonar.android.shared.repos

import com.awonar.android.model.feed.*
import com.awonar.android.shared.api.FeedService
import com.molysulfur.library.network.DirectNetworkFlow
import com.molysulfur.library.result.Result
import kotlinx.coroutines.flow.Flow
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


    fun getAllFeed(type: String, page: Int) =
        object : DirectNetworkFlow<Int, FeedPaging, FeedResponse>() {
            override fun createCall(): Response<FeedResponse> =
                service.getAllFeed(type = type, page = page).execute()

            override fun convertToResultType(response: FeedResponse): FeedPaging {
                return FeedPaging(
                    feeds = response.feeds ?: emptyList(),
                    page = if (response.meta?.hasMore == true) response.meta?.page?.plus(1)
                        ?: 0 else 0
                )
            }

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }

        }.asFlow()

    fun create(request: CreateFeed): Flow<Result<Feed?>> =
        object : DirectNetworkFlow<CreateFeed, Feed?, Feed?>() {
            override fun createCall(): Response<Feed?> =
                service.create(request).execute()

            override fun convertToResultType(response: Feed?): Feed? = response

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }

        }.asFlow()

}