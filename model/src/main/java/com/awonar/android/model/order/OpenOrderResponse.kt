package com.awonar.android.model.order

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class OpenOrderResponse(
    @SerializedName("id") val id: String?,
    @SerializedName("copyId") val copyId: String?,
    @SerializedName("instrumentId") val instrumentId: Int,
    @SerializedName("parentOrderId") val parentOrderId: String?,
    @SerializedName("ip") val ip: String?,
    @SerializedName("amount") val amount: Int,
    @SerializedName("exposure") val exposure: Float,
    @SerializedName("isBuy") val isBuy: Boolean,
    @SerializedName("leverage") val leverage: Int,
    @SerializedName("openDateTime") val openDateTime: String?,
    @SerializedName("orderNo") val orderNo: String?,
    @SerializedName("rate") val rate: Float,
    @SerializedName("reason") val reason: String?,
    @SerializedName("stopLossRate") val stopLossRate: Float,
    @SerializedName("takeProfitRate") val takeProfitRate: Float,
    @SerializedName("units") val units: Float,
) : Parcelable