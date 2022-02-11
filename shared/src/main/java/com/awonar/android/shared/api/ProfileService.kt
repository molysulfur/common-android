package com.awonar.android.shared.api

import com.awonar.android.model.user.DrawdownResponse
import com.awonar.android.model.user.StatGainResponse
import com.awonar.android.model.user.StatRiskResponse
import com.awonar.android.shared.constrant.BuildConfig
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProfileService {

    @GET("v1/users/stats/gain/days/{uid}")
    fun getGrowthDay(
        @Path("uid") uid: String,
        @Query("year") year: String,
    ): Call<HashMap<String, Float>>

    @GET("v1/users/stats/{uid}")
    fun getDrawdown(@Path("uid") uid: String): Call<DrawdownResponse>

    @GET("v1/users/stats/risk/history/{uid}")
    fun getRiskStatistic(@Path("uid") uid: String): Call<StatRiskResponse>

    @GET("v1/users/stats/gain/{uid}")
    fun getGrowthStatistic(@Path("uid") uid: String): Call<StatGainResponse>

    companion object {

        fun create(client: NetworkClient): ProfileService = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client.getClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProfileService::class.java)

    }
}