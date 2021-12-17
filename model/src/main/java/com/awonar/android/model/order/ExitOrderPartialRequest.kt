package com.awonar.android.model.order

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExitOrderPartialRequest(
    @SerializedName("positionId") val positionId: String?,
    @SerializedName("unitsToDeduce") val unitsToDeduce: Float,
//    val marketOrderType: MarketOrderType,
) : Parcelable