package com.awonar.android.shared.api

import com.awonar.android.model.feed.Feed
import com.awonar.android.model.feed.FeedResponse
import com.awonar.android.model.feed.NewsMetaResponse
import com.awonar.android.shared.constrant.BuildConfig
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface FeedService {

    @Multipart
    @POST("v1/feed/posts")
    fun create(
        @PartMap request: MutableMap<String, RequestBody>,
        @Part imageParts: List<MultipartBody.Part>
    ): Call<Feed?>

    @GET("v1/link-preview")
    fun getMetaNews(@Query("url") url: String): Call<NewsMetaResponse?>

    @GET("v1/feed/posts/{type}")
    fun getAllFeed(
        @Path("type") type: String = "",
        @Query("page") page: Int,
        @Query("limit") limit: Int = 5,
    ): Call<FeedResponse>

    @GET("v1/feed/posts/{type}/{prefix}")
    fun getAllFeed(
        @Path("type") type: String = "",
        @Path("prefix") prefix: String = "",
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