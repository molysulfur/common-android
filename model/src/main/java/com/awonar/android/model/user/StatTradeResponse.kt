package com.awonar.android.model.user

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class StatTradeResponse(
    @SerializedName("avgLoss") val avgLoss: Float,
    @SerializedName("avgProfit") val avgProfit: Float,
    @SerializedName("investments") val investmentStats: List<StatInvestment>,
    @SerializedName("performanceTrades") val performanceTrades: List<StatPerformanceTrade>,
    @SerializedName("profitable") val profitable: Float,
    @SerializedName("tradeTotal") val tradeTotal: Int,
) : Parcelable

@Parcelize
data class StatPerformanceTrade(
    @SerializedName("category") val category :String?,
    @SerializedName("ratio") val ratio :Float
) : Parcelable

@Parcelize
data class StatInvestment(
    @SerializedName("avgLoss") val avgLoss: Float,
    @SerializedName("avgProfit") val avgProfit: Float,
    @SerializedName("instrumentId") val instrumentId: Int,
    @SerializedName("symbol") val symbol: String?,
    @SerializedName("totalTrades") val totalTrades: Int,
    @SerializedName("tradesRatio") val tradesRatio: Float,
    @SerializedName("winRatio") val winRatio: Float,
) : Parcelable

