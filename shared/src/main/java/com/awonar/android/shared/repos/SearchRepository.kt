package com.awonar.android.shared.repos

import com.awonar.android.model.search.Search
import com.awonar.android.model.search.SearchRequest
import com.awonar.android.model.search.SearchResponse
import com.awonar.android.shared.api.SearchService
import com.molysulfur.library.network.DirectNetworkFlow
import retrofit2.Response
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val service: SearchService,
) {

    fun clearRecently() = object :
        DirectNetworkFlow<String, Unit, List<Search>>() {
        override fun createCall(): Response<List<Search>> =
            service.clearRecent().execute()

        override fun convertToResultType(response: List<Search>): Unit = Unit

        override fun onFetchFailed(errorMessage: String) {
            println(errorMessage)
        }

    }.asFlow()


    fun getRecentlySearch() = object :
        DirectNetworkFlow<String, List<Search>, List<Search>>() {
        override fun createCall(): Response<List<Search>> =
            service.getRecently().execute()

        override fun convertToResultType(response: List<Search>): List<Search> = response

        override fun onFetchFailed(errorMessage: String) {
            println(errorMessage)
        }

    }.asFlow()

    fun getSearch(request: SearchRequest) = object :
        DirectNetworkFlow<SearchRequest, SearchResponse?, SearchResponse?>() {
        override fun createCall(): Response<SearchResponse?> =
            service.search(request).execute()

        override fun convertToResultType(response: SearchResponse?): SearchResponse? = response

        override fun onFetchFailed(errorMessage: String) {
            println(errorMessage)
        }

    }.asFlow()
}