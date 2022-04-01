package com.awonar.android.model.user

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class DrawdownResponse(
    @SerializedName("dailyDrawdown") val dailyDrawdown: Float,
    @SerializedName("weeklyDrawdown") val weeklyDrawdown: Float,
    @SerializedName("yearlyDrawdown") val yearlyDrawdown: Float,
    @SerializedName("risk") val risk: Int,
) : Parcelable

