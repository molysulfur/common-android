package com.awonar.android.model.tradingactivity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TradingActivityRequest(
    @SerializedName("displayFullName") val isDisplayFullName: Boolean,
    @SerializedName("private") val isPrivate: Boolean
) : Parcelable