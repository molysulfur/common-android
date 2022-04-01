package com.awonar.android.shared.api


import com.awonar.android.model.marketprofile.FinancialResponse
import com.awonar.android.model.marketprofile.MarketOverview
import com.awonar.android.shared.constrant.BuildConfig
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface MarketProfileService {

    @GET("v1/instruments/stats/{instrumentId}")
    fun getOverView(@Path("instrumentId") instrumentId: Int): Call<MarketOverview>

    @GET("v1/instruments/financial/{instrumentId}")
    fun getFinancial(@Path("instrumentId") instrumentId: Int): Call<FinancialResponse>


    companion object {

        fun create(client: NetworkClient): MarketProfileService = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client.getClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MarketProfileService::class.java)

    }
}