package com.awonar.android.shared.api

import com.awonar.android.model.portfolio.Portfolio
import com.awonar.android.model.portfolio.UserPortfolioResponse
import com.awonar.android.shared.constrant.BuildConfig
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface PortfolioService {

    @GET("/api/v1/users/info")
    fun getUserPortFolio() :Call<UserPortfolioResponse>

    @GET("api/v1/portfolios")
    fun getPortFolio(): Call<Portfolio>

    companion object {

        fun create(client: NetworkClient): PortfolioService = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client.getClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PortfolioService::class.java)

    }
}