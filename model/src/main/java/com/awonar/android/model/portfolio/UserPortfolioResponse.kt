package com.awonar.android.model.portfolio

import android.os.Parcelable
import com.awonar.android.model.market.Instrument
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserPortfolioResponse(
    @SerializedName("entryOrder") val entries: List<EntryOrderPortfolio>,
    @SerializedName("orders") val orders: List<OrderPortfolio>,
    @SerializedName("positions") val positions: List<Position>
) : Parcelable

@Parcelize
data class EntryOrderPortfolio(
    @SerializedName("id") val id: String,
    @SerializedName("instrumentId") val instrumentId: Int,
    @SerializedName("copyId") val copyId: String,
    @SerializedName("amount") val amount: Float,
    @SerializedName("exposure") val exposure: Float,
    @SerializedName("initOpenRate") val initOpenRate: Float,
    @SerializedName("ip") val ip: Float,
    @SerializedName("isBuy") val isBuy: Boolean,
    @SerializedName("leverage") val leverage: Int,
    @SerializedName("openDateTime") val openDateTime: String,
    @SerializedName("orderNo") val orderNo: String,
    @SerializedName("parentPositionId") val parentPositionId: String,
    @SerializedName("parentUid") val parentUid: String,
    @SerializedName("parentUsername") val parentUsername: String,
    @SerializedName("reason") val reason: String,
    @SerializedName("stopLossPercentage") val stopLossPercentage: Float,
    @SerializedName("takeProfitPercentage") val takeProfitPercentage: Float,
) : Parcelable


@Parcelize
data class OrderPortfolio(
    @SerializedName("id") val id: String,
    @SerializedName("instrumentId") val instrumentId: Int,
    @SerializedName("copyId") val copyId: String,
    @SerializedName("amount") val amount: Float,
    @SerializedName("exposure") val exposure: Float,
    @SerializedName("ip") val ip: String,
    @SerializedName("isBuy") val isBuy: Boolean,
    @SerializedName("leverage") val leverage: Int,
    @SerializedName("openDateTime") val openDateTime: String,
    @SerializedName("orderNo") val orderNo: String,
    @SerializedName("parentOrderId") val parentOrderId: String,
    @SerializedName("rate") val rate: Float,
    @SerializedName("reason") val reason: String,
    @SerializedName("stopLossRate") val stopLossRate: Float,
    @SerializedName("takeProfitRate") val takeProfitRate: Float,
    @SerializedName("units") val units: Float,
) : Parcelable

@Parcelize
data class Position(
    @SerializedName("id") val id: String,
    @SerializedName("ip") val ip: String,
    @SerializedName("isBuy") val isBuy: Boolean,
    @SerializedName("leverage") val leverage: Int,
    @SerializedName("netProfit") val netProfit: Float,
    @SerializedName("instrumentId") val instrumentId: Int,
    @SerializedName("copyId") val copyId: String,
    @SerializedName("amount") val amount: Float,
    @SerializedName("exitOrder") val exitOrder: ExitOrder,
    @SerializedName("exposure") val exposure: Float,
    @SerializedName("instrument") val instrument: Instrument,
    @SerializedName("openDateTime") val openDateTime: String,
    @SerializedName("openRate") val openRate: Float,
    @SerializedName("orderNo") val orderNo: String,
    @SerializedName("parentPositionId") val parentPositionId: String,
    @SerializedName("parentUid") val parentUid: String,
    @SerializedName("parentUsername") val parentUsername: String,
    @SerializedName("portfolioId") val portfolioId: String,
    @SerializedName("positionNo") val positionNo: String,
    @SerializedName("reason") val reason: String,
    @SerializedName("stopLossRate") val stopLossRate: Float,
    @SerializedName("takeProfitRate") val takeProfitRate: Float,
    @SerializedName("totalFees") val totalFees: Float,
    @SerializedName("units") val units: Float,
) : Parcelable


@Parcelize
data class ExitOrder(
    @SerializedName("id") val id: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("ip") val ip: String,
    @SerializedName("isManagerAction") val isManagerAction: Boolean,
    @SerializedName("portfolioId") val portfolioId: String,
    @SerializedName("positionId") val positionId: String,
    @SerializedName("reason") val reason: String,
    @SerializedName("unitsToDeduce") val unitsToDeduce: Float,
) : Parcelable