package com.awonar.android.model.portfolio

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class PublicPosition(
    @SerializedName("averageOpen") val avgOpen: Float,
    @SerializedName("equity") val equity: Float,
    @SerializedName("instrumentId") val instrumentId: Int,
    @SerializedName("invested") val invested: Float,
    @SerializedName("netProfit") val netProfit: Float,
    @SerializedName("positions") val positions: List<Position>,
    @SerializedName("symbol") val symbol: String?,
) : Parcelable

