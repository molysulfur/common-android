package com.awonar.android.model.portfolio

import android.os.Parcelable
import com.awonar.android.model.core.Meta
import com.awonar.android.model.market.Instrument
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class HistoryPositionResponse(
    @SerializedName("markets") val markets: List<HistoryPosition>,
    @SerializedName("meta") val meta: Meta
) : Parcelable

@Parcelize
data class HistoryPosition(
    @SerializedName("instrument") val instrument: Instrument?,
    @SerializedName("instrumentId") val instrumentId: Int,
    @SerializedName("netProfitPercentage") val netProfitPercentage: Float,
    @SerializedName("profitabilityPercentage") val profitabilityPercentage: Float,
    @SerializedName("symbol") val symbol: String?,
    @SerializedName("totalPositions") val totalPositions: Int,
) : Parcelable

