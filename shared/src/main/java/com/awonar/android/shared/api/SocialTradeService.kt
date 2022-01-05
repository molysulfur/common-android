package com.awonar.android.shared.api

import com.awonar.android.model.socialtrade.CopierRecommendedResponse
import com.awonar.android.shared.constrant.BuildConfig
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface SocialTradeService {

    @GET("v1/users/recommend/traders")
    fun getRecommended(): Call<CopierRecommendedResponse>

    companion object {

        fun create(client: NetworkClient): SocialTradeService = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client.getClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SocialTradeService::class.java)

    }
}