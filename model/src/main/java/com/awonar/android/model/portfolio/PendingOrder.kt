package com.awonar.android.model.portfolio

import android.os.Parcelable
import com.awonar.android.model.market.Instrument
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PendingOrder(
    @SerializedName("id") val id: String,
    @SerializedName("amount") val amount: Float,
    @SerializedName("initOpenRate") val initOpenRate: Float,
    @SerializedName("instrument") val instrument: Instrument,
    @SerializedName("instrumentId") val instrumentId: Int,
    @SerializedName("isBuy") val isBuy: Boolean,
    @SerializedName("leverage") val leverage: Int,
    @SerializedName("openDateTime") val openDateTime: String?,
    @SerializedName("orderNo") val orderNo: String?,
    @SerializedName("parentUsername") val parentUsername: String?,
    @SerializedName("rate") val rate: Float,
    @SerializedName("reason") val reason: String?,
    @SerializedName("stopLossPercentage") val stopLossPercentage: Float,
    @SerializedName("stopLossRate") val stopLossRate: Float,
    @SerializedName("takeProfitPercentage") val takeProfitPercentage: Float,
    @SerializedName("takeProfitRate") val takeProfitRate: Float,
    @SerializedName("type") val type: String,
    @SerializedName("units") val units: Float,
) : Parcelable

