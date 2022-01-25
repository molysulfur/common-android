package com.awonar.android.shared.api

import com.awonar.android.model.watchlist.WatchlistFolder
import com.awonar.android.shared.constrant.BuildConfig
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface WatchlistService {

    @GET("v1/watchlist/folder")
    fun getFolders(): Call<List<WatchlistFolder>>

    companion object {

        fun create(client: NetworkClient): WatchlistService = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client.getClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WatchlistService::class.java)

    }
}