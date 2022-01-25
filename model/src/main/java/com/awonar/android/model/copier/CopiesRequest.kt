package com.awonar.android.model.copier

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CopiesRequest(
    @SerializedName("copyExistingPositions") val copyExistingPositions: Boolean = false,
    @SerializedName("stopLossPercentage") val stopLossPercentage: Float = 0f,
    @SerializedName("initialInvestment") val initialInvestment: Float = 0f,
    @SerializedName("parentUserId") val parentUserId: String = "",
) : Parcelable