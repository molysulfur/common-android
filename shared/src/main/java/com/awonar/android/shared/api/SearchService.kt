package com.awonar.android.shared.api

import com.awonar.android.model.search.Search
import com.awonar.android.model.search.SearchRequest
import com.awonar.android.model.search.SearchResponse
import com.awonar.android.shared.constrant.BuildConfig
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface SearchService {

    @DELETE("v1/search")
    fun clearRecent() : Call<List<Search>>

    @GET("v1/search/mobile")
    fun getRecently(): Call<List<Search>>

    @POST("v1/search/mobile")
    fun search(@Body request: SearchRequest): Call<SearchResponse?>

    companion object {

        fun create(client: NetworkClient): SearchService = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client.getClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SearchService::class.java)

    }
}