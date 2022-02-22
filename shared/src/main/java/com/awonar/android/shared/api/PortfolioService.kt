package com.awonar.android.shared.api

import com.awonar.android.model.portfolio.*
import com.awonar.android.shared.constrant.BuildConfig
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface PortfolioService {

    @GET("v1/positions/public/markets/{username}")
    fun getUserPositionManual(@Path("username") username: String): Call<UserPortfolioResponse>

    @GET("v1/pending-orders")
    fun getPendingOrders(): Call<List<PendingOrder>>

    @GET("v1/users/info")
    fun getMyPositionManual(): Call<UserPortfolioResponse>

    @GET("v1/positions")
    fun getMyPositions(): Call<List<Position>>

    @GET("v1/copy")
    fun getMyCopier(): Call<List<Copier>>

    @GET("v1/portfolios")
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