package com.awonar.android.shared.api

import com.awonar.android.model.feed.CreateFeed
import com.awonar.android.model.feed.Feed
import com.awonar.android.model.feed.FeedResponse
import com.awonar.android.model.feed.NewsMetaResponse
import com.awonar.android.shared.constrant.BuildConfig
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface FeedService {

    @POST("v1/feed/posts")
    fun create(@Body request: CreateFeed): Call<Feed?>

    @GET("v1/link-preview")
    fun getMetaNews(@Query("url") url: String): Call<NewsMetaResponse?>

    @GET("v1/feed/posts/{type}")
    fun getAllFeed(
        @Path("type") type: String = "",
        @Query("page") page: Int,
        @Query("limit") limit: Int = 5,
    ): Call<FeedResponse>

    companion object {

        fun create(client: NetworkClient): FeedService = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client.getClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FeedService::class.java)

    }
}