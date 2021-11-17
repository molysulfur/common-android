package com.awonar.android.model.history

import android.os.Parcelable
import com.awonar.android.model.user.Master
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MarketHistoryResponse(
    @SerializedName("markets") val markets: List<MarketHistory>,
    @SerializedName("meta") val meta: HistoryMeta
) : Parcelable

@Parcelize
data class MarketHistory(
    @SerializedName("endEquity") val endEquity: Float,
    @SerializedName("instrumentId") val instrumentId: Int?,
    @SerializedName("symbol") val symbol: String?,
    @SerializedName("totalFees") val totalFees: Float,
    @SerializedName("totalInvested") val totalInvested: Float,
    @SerializedName("totalNetProfit") val totalNetProfit: Float,
    @SerializedName("totalPositions") val totalPositions: Float,
    @SerializedName("user") val user: Master?,
) : Parcelable

