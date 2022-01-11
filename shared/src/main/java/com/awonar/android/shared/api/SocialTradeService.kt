package com.awonar.android.shared.api

import com.awonar.android.model.copier.UpdateFundRequest
import com.awonar.android.model.copier.CopiesRequest
import com.awonar.android.model.copier.PauseCopyRequest
import com.awonar.android.model.portfolio.Copier
import com.awonar.android.model.socialtrade.TradersResponse
import com.awonar.android.shared.constrant.BuildConfig
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface SocialTradeService {

    @POST("v1/copy/pause")
    fun updatePauseCopy(@Body body: PauseCopyRequest): Call<Copier>

    @POST("v1/copy/fund")
    fun addFund(@Body body: UpdateFundRequest): Call<Copier>

    @POST("v1/copy")
    fun createCopy(@Body copy: CopiesRequest): Call<Copier>

    @DELETE("v1/copy/{id}")
    fun stopCopy(@Path("id") id: String): Call<Copier?>

    @GET("v1/users/recommend/traders")
    fun getRecommended(): Call<TradersResponse>

    @GET("v1/users/traders")
    fun getTraders(
        @Query("verified") verified: Boolean = true,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 5,
        @Query("period") period: String? = "3MonthsAgo",
        @Query("sort") sort: String? = "-gain",
        @Query("maxRisk") maxRisk: Int = 6,
        @Query("uid") uid: String? = null,
    ): Call<TradersResponse>

    companion object {

        fun create(client: NetworkClient): SocialTradeService = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client.getClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SocialTradeService::class.java)

    }
}