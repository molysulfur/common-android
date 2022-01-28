package com.awonar.android.shared.api

import com.awonar.android.model.core.MessageSuccessResponse
import com.awonar.android.model.watchlist.AddWatchlistRequest
import com.awonar.android.model.watchlist.WatchlistFolder
import com.awonar.android.shared.constrant.BuildConfig
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface WatchlistService {

    @PUT("v1/watchlist/folder/{folderId}/default")
    fun setDefault(@Path("folderId") folderId: String): Call<MessageSuccessResponse>

    @GET("v1/watchlist/folder/mobile")
    fun getFolders(): Call<List<WatchlistFolder>>

    @POST("v1/watchlist/folder")
    fun addFolder(@Body request: AddWatchlistRequest): Call<WatchlistFolder>

    @DELETE("v1/watchlist/folder/{id}")
    fun deleteFolder(@Path("id") id: String): Call<MessageSuccessResponse>

    @DELETE("v1/watchlist/items/{id}")
    fun deleteItem(@Path("id") id: String): Call<MessageSuccessResponse>

    companion object {

        fun create(client: NetworkClient): WatchlistService = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client.getClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WatchlistService::class.java)

    }
}