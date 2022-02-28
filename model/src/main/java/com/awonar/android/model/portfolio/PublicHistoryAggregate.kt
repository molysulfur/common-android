package com.awonar.android.model.portfolio

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PublicHistoryAggregate(
    @SerializedName("totalItems") val tradeCount: Int,
    @SerializedName("totalNetProfitPercentage") val totalNetProfitPercentage: Float,
    @SerializedName("totalProfitabilityPercentage") val totalProfitabilityPercentage: Float,
) : Parcelable