package com.awonar.android.model.order

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class OpenOrderRequest(
    @SerializedName("instrumentId") val instrumentId: Int,
    @SerializedName("amount") val amount: Float,
    @SerializedName("isBuy") val isBuy: Boolean,
    @SerializedName("leverage") val leverage: Int,
    @SerializedName("rate") val rate: Float,
    @SerializedName("stopLossRate") val stopLoss: Float,
    @SerializedName("takeProfitRate") val takeProfit: Float,
    @SerializedName("units") val units: Float,
) : Parcelable