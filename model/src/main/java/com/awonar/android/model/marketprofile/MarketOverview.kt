package com.awonar.android.model.marketprofile

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class MarketOverview(
    @SerializedName("dayRange") val dayRange: HighLow,
    @SerializedName("info") val info: MarketProfileInfo,
    @SerializedName("prevClose") val prevClose: Float,
    @SerializedName("yearRange") val yearRange: HighLow,
    @SerializedName("yearReturn") val yearReturn: Float,
) : Parcelable

@Parcelize
data class HighLow(
    @SerializedName("low") val low: Float,
    @SerializedName("high") val high: Float,
) : Parcelable

@Parcelize
data class MarketProfileInfo(
    @SerializedName("marketCap") val marketCap: Long,
    @SerializedName("peRatio") val peRatio: Float,
    @SerializedName("revenue") val revenue: Long,
    @SerializedName("averageVolume") val averageVolume: Long,
    @SerializedName("beta") val beta: Float,
    @SerializedName("dividend") val dividend: Float,
    @SerializedName("eps") val eps: Float,
) : Parcelable
