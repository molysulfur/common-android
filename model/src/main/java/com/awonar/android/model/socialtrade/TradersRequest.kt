package com.awonar.android.model.socialtrade

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import retrofit2.http.Query


@Parcelize
data class TradersRequest(
    @Query("uid") val uid: String? = null,
    @Query("displayFullName") val displayFullName: Boolean? = null,
    @Query("verified") val verified: Boolean? = null,
    @Query("popular") val popular: String? = null,
    @Query("minActiveWeek") val minActiveWeek: Int? = null,
    @Query("maxActiveWeek") val maxActiveWeek: Int? = null,
    @Query("minProfitablePercentage") val minProfitablePercentage: Float? = null,
    @Query("maxAllocatedPercentage") val maxAllocatedPercentage: Float? = null,
    @Query("minCopiers") val minCopiers: Int? = null,
    @Query("maxCopiers") val maxCopiers: Int? = null,
    @Query("minWeeklyDrawdown") val minWeeklyDrawdown: Float? = null,
    @Query("maxWeeklyDrawdown") val maxWeeklyDrawdown: Float? = null,
    @Query("minDailyDrawdown") val minDailyDrawdown: Float? = null,
    @Query("maxDailyDrawdown") val maxDailyDrawdown: Float? = null,
    @Query("minGain") val minGain: Float? = null,
    @Query("maxGain") val maxGain: Float? = null,
    @Query("minMaxRisk") val minMaxRisk: Int? = null,
    @Query("maxMaxRisk") val maxMaxRisk: Int? = null,
    @Query("minRisk") val minRisk: Int? = null,
    @Query("maxRisk") val maxRisk: Int? = null,
    @Query("minTrades") val minTrades: Int? = null,
    @Query("maxTrades") val maxTrades: Int? = null,
    @Query("minAllocatedPercentage") val minAllocatedPercentage: Float? = null,
    @Query("maxProfitablePercentage") val maxProfitablePercentage: Float? = null,
    @Query("period") val period: List<String>? = null,
    @Query("sort") val sort: List<String>? = null,
    @Query("allocated") val allocated: List<String>? = null,
    @Query("page") val page: Int = 1,
    @Query("limit") val limit: Int = 5,
    @Query("country") val country: List<String>? = null,
) : Parcelable {
    fun toMap(): Map<String, String?> {
        return mapOf(
            "uid" to uid,
            "displayFullName" to displayFullName.toString(),
            "verified" to verified.toString(),
            "popular" to popular.toString(),
            "minActiveWeek" to minActiveWeek.toString(),
            "maxActiveWeek" to maxActiveWeek.toString(),
            "minProfitablePercentage" to minProfitablePercentage.toString(),
            "maxAllocatedPercentage" to maxAllocatedPercentage.toString(),
            "minCopiers" to minCopiers.toString(),
            "maxCopiers" to maxCopiers.toString(),
            "minWeeklyDrawdown" to minWeeklyDrawdown.toString(),
            "maxWeeklyDrawdown" to maxWeeklyDrawdown.toString(),
            "minDailyDrawdown" to minDailyDrawdown.toString(),
            "maxDailyDrawdown" to maxDailyDrawdown.toString(),
            "minGain" to minGain.toString(),
            "maxGain" to maxGain.toString(),
            "minMaxRisk" to minMaxRisk.toString(),
            "maxMaxRisk" to maxMaxRisk.toString(),
            "minRisk" to minRisk.toString(),
            "maxRisk" to maxRisk.toString(),
            "minTrades" to minTrades.toString(),
            "maxTrades" to maxTrades.toString(),
            "period" to period?.joinToString(","),
            "sort" to sort?.joinToString(","),
            "allocated" to allocated?.joinToString(","),
            "page" to page.toString(),
            "limit" to limit.toString(),
            "minAllocatedPercentage" to minAllocatedPercentage.toString(),
            "maxProfitablePercentage" to maxProfitablePercentage.toString(),
            "country" to country?.joinToString(","),
        )
    }
}


