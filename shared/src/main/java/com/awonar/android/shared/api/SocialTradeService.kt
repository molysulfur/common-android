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
        @Query("uid") uid: List<String>? = null,
        @Query("displayFullName") displayFullName: List<String>? = null,
        @Query("verified") verified: List<String>? = null,
        @Query("popular") popular: List<String>? = null,
        @Query("minActiveWeek") minActiveWeek: List<String>? = null,
        @Query("maxActiveWeek") maxActiveWeek: List<String>? = null,
        @Query("minProfitablePercentage") minProfitablePercentage: List<String>? = null,
        @Query("maxAllocatedPercentage") maxAllocatedPercentage: List<String>? = null,
        @Query("minCopiers") minCopiers: List<String>? = null,
        @Query("maxCopiers") maxCopiers: List<String>? = null,
        @Query("minWeeklyDrawdown") minWeeklyDrawdown: List<String>? = null,
        @Query("maxWeeklyDrawdown") maxWeeklyDrawdown: List<String>? = null,
        @Query("minDailyDrawdown") minDailyDrawdown: List<String>? = null,
        @Query("maxDailyDrawdown") maxDailyDrawdown: List<String>? = null,
        @Query("minGain") minGain: List<String>? = null,
        @Query("maxGain") maxGain: List<String>? = null,
        @Query("minMaxRisk") minMaxRisk: List<String>? = null,
        @Query("maxMaxRisk") maxMaxRisk: List<String>? = null,
        @Query("minRisk") minRisk: List<String>? = null,
        @Query("maxRisk") maxRisk: List<String>? = null,
        @Query("minTrades") minTrades: List<String>? = null,
        @Query("maxTrades") maxTrades: List<String>? = null,
        @Query("period") period: List<String>? = listOf("1MonthAgo"),
        @Query("sort") sort: List<String>? = null,
        @Query("allocated") allocated: List<String>? = null,
        @Query("country") country: List<String>? = null,
        @Query("minAllocatedPercentage") minAllocatedPercentage: List<String>? = null,
        @Query("maxProfitablePercentage") maxProfitablePercentage: List<String>? = null,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 5,
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