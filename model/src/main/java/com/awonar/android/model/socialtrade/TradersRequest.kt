package com.awonar.android.model.socialtrade

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import retrofit2.http.Query


@Parcelize
data class TradersRequest(
    @Query("uid") var uid: List<String>? = null,
    @Query("displayFullName") var displayFullName: List<String>? = null,
    @Query("verified") var verified: List<String>? = null,
    @Query("popular") var popular: List<String>? = null,
    @Query("minActiveWeek") var minActiveWeek: List<String>? = null,
    @Query("maxActiveWeek") var maxActiveWeek: List<String>? = null,
    @Query("minProfitablePercentage") var minProfitablePercentage: List<String>? = null,
    @Query("maxAllocatedPercentage") var maxAllocatedPercentage: List<String>? = null,
    @Query("minCopiers") var minCopiers: List<String>? = null,
    @Query("maxCopiers") var maxCopiers: List<String>? = null,
    @Query("minWeeklyDrawdown") var minWeeklyDrawdown: List<String>? = null,
    @Query("maxWeeklyDrawdown") var maxWeeklyDrawdown: List<String>? = null,
    @Query("minDailyDrawdown") var minDailyDrawdown: List<String>? = null,
    @Query("maxDailyDrawdown") var maxDailyDrawdown: List<String>? = null,
    @Query("minGain") var minGain: List<String>? = null,
    @Query("maxGain") var maxGain: List<String>? = null,
    @Query("minMaxRisk") var minMaxRisk: List<String>? = null,
    @Query("maxMaxRisk") var maxMaxRisk: List<String>? = null,
    @Query("minRisk") var minRisk: List<String>? = null,
    @Query("maxRisk") var maxRisk: List<String>? = null,
    @Query("minTrades") var minTrades: List<String>? = null,
    @Query("maxTrades") var maxTrades: List<String>? = null,
    @Query("minAllocatedPercentage") var minAllocatedPercentage: List<String>? = null,
    @Query("maxProfitablePercentage") var maxProfitablePercentage: List<String>? = null,
    @Query("period") var period: List<String>? = null,
    @Query("sort") var sort: List<String>? = null,
    @Query("allocated") var allocated: List<String>? = null,
    @Query("page") var page: Int = 1,
    @Query("limit") var limit: Int = 5,
    @Query("country") var country: List<String>? = null,
) : Parcelable {

    fun toMap(): Map<String, String?> {
        return mapOf(
            "uid" to uid?.joinToString(","),
            "displayFullName" to displayFullName?.joinToString(","),
            "verified" to verified?.joinToString(","),
            "popular" to popular?.joinToString(","),
            "minActiveWeek" to minActiveWeek?.joinToString(","),
            "maxActiveWeek" to maxActiveWeek?.joinToString(","),
            "minProfitablePercentage" to minProfitablePercentage?.joinToString(","),
            "maxAllocatedPercentage" to maxAllocatedPercentage?.joinToString(","),
            "minCopiers" to minCopiers?.joinToString(","),
            "maxCopiers" to maxCopiers?.joinToString(","),
            "minWeeklyDrawdown" to minWeeklyDrawdown?.joinToString(","),
            "maxWeeklyDrawdown" to maxWeeklyDrawdown?.joinToString(","),
            "minDailyDrawdown" to minDailyDrawdown?.joinToString(","),
            "maxDailyDrawdown" to maxDailyDrawdown?.joinToString(","),
            "minGain" to minGain?.joinToString(","),
            "maxGain" to maxGain?.joinToString(","),
            "minMaxRisk" to minMaxRisk?.joinToString(","),
            "maxMaxRisk" to maxMaxRisk?.joinToString(","),
            "minRisk" to minRisk?.joinToString(","),
            "maxRisk" to maxRisk?.joinToString(","),
            "minTrades" to minTrades?.joinToString(","),
            "maxTrades" to maxTrades?.joinToString(","),
            "period" to period?.joinToString(","),
            "sort" to sort?.joinToString(","),
            "allocated" to allocated?.joinToString(","),
            "minAllocatedPercentage" to minAllocatedPercentage?.joinToString(","),
            "maxProfitablePercentage" to maxProfitablePercentage?.joinToString(","),
            "country" to country?.joinToString(","),
        )
    }
}

