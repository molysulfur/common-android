package com.awonar.android.shared.api

import com.awonar.android.model.user.StatGainResponse
import com.awonar.android.shared.constrant.BuildConfig
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ProfileService {

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