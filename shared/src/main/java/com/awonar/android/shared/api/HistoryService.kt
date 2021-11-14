package com.awonar.android.shared.api

import com.awonar.android.model.history.Aggregate
import com.awonar.android.model.history.HistoryResponse
import com.awonar.android.shared.constrant.BuildConfig
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface HistoryService {

    @GET("/api/v1/history")
    suspend fun getHistory(
        @Query("limit") limit: Int = 10,
        @Query("page") page: Int = 1,
        @Query("startDate") startDate: Long,
    ): HistoryResponse

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

