package com.awonar.android.model.market

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Quote(
    @SerializedName("id") val id: Int,
    @SerializedName("ask") val ask: Float,
    @SerializedName("bid") val bid: Float,
    @SerializedName("askS") val askSpread: Float,
    @SerializedName("bidS") val bidSpread: Float,
    @SerializedName("prev") val previous: Float,
    @SerializedName("close") val close: Float,
    @SerializedName("ts") val timestamp: Long,
    @SerializedName("status") val status: String?
) : Parcelable