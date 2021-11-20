package com.awonar.android.shared.api

import com.awonar.android.model.history.*
import com.awonar.android.shared.constrant.BuildConfig
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HistoryService {

    @GET("/api/v1/history/copy/{username}")
    fun getCopiesHistory(
        @Path("username") username: String, @Query("page") page: Int = 1,
        @Query("filter") filter: String = "",
        @Query("startDate") startDate: Long,
    ): Call<CopiesAggregateResponse?>

    @GET("/api/v1/history")
    fun getHistory(
        @Query("limit") limit: Int = 10,
        @Query("page") page: Int = 1,
        @Query("filter") filter: String = "",
        @Query("startDate") startDate: Long,
    ): Call<HistoryResponse>

    @GET("/api/v1/history")
    fun filterHistory(
        @Query("symbol") symbol: String,
        @Query("limit") limit: Int = 10,
        @Query("page") page: Int = 1,
        @Query("filter") filter: String = "",
        @Query("startDate") startDate: Long,
    ): Call<HistoryResponse>

    @GET("/api/v1/history/markets")
    fun getMarketHistory(
        @Query("limit") limit: Int = 10,
        @Query("page") page: Int = 1,
        @Query("startDate") startDate: Long,
    ): Call<MarketHistoryResponse>

    @GET("/api/v1/history/markets")
    fun filterMarketHistory(
        @Query("symbol") symbol: String,
        @Query("limit") limit: Int = 10,
        @Query("page") page: Int = 1,
        @Query("filter") filter: String = "",
        @Query("startDate") startDate: Long,
    ): Call<MarketHistoryResponse>

    @GET("/api/v1/history/aggregate")
    fun getAggregate(@Query("startDate") startDate: Long): Call<Aggregate>

    companion object {

        fun create(client: NetworkClient): HistoryService = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client.getClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HistoryService::class.java)
    }
}

