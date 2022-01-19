package com.awonar.android.shared.api

import com.awonar.android.model.copier.UpdateFundRequest
import com.awonar.android.model.copier.CopiesRequest
import com.awonar.android.model.copier.PauseCopyRequest
import com.awonar.android.model.copier.UpdateCopy
import com.awonar.android.model.portfolio.Copier
import com.awonar.android.model.socialtrade.TradersRequest
import com.awonar.android.model.socialtrade.TradersResponse
import com.awonar.android.shared.constrant.BuildConfig
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface SocialTradeService {

    @POST("v1/copy/sl")
    fun updateCopy(@Body body: UpdateCopy): Call<Copier>

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
        @Query("uid") uid: String? = null,
        @Query("displayFullName") displayFullName: Boolean? = null,
        @Query("verified") verified: Boolean? = null,
        @Query("popular") popular: String? = null,
        @Query("minActiveWeek") minActiveWeek: Int? = null,
        @Query("maxActiveWeek") maxActiveWeek: Int? = null,
        @Query("minProfitablePercentage") minProfitablePercentage: Float? = null,
        @Query("maxAllocatedPercentage") maxAllocatedPercentage: Float? = null,
        @Query("minCopiers") minCopiers: Int? = null,
        @Query("maxCopiers") maxCopiers: Int? = null,
        @Query("minWeeklyDrawdown") minWeeklyDrawdown: Float? = null,
        @Query("maxWeeklyDrawdown") maxWeeklyDrawdown: Float? = null,
        @Query("minDailyDrawdown") minDailyDrawdown: Float? = null,
        @Query("maxDailyDrawdown") maxDailyDrawdown: Float? = null,
        @Query("minGain") minGain: Float? = null,
        @Query("maxGain") maxGain: Float? = null,
        @Query("minMaxRisk") minMaxRisk: Int? = null,
        @Query("maxMaxRisk") maxMaxRisk: Int? = null,
        @Query("minRisk") minRisk: Int? = null,
        @Query("maxRisk") maxRisk: Int? = null,
        @Query("minTrades") minTrades: Int? = null,
        @Query("maxTrades") maxTrades: Int? = null,
        @Query("period") period: List<String>? = listOf("1MonthAgo"),
        @Query("sort") sort: List<String>? = null,
        @Query("allocated") allocated: List<String>? = null,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 5,
        @Query("country") country: List<String>? = null,
        @Query("minAllocatedPercentage") minAllocatedPercentage: Float? = null,
        @Query("maxProfitablePercentage") maxProfitablePercentage: Float? = null,
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