package com.awonar.android.model.order

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExitOrderRequest(
    @SerializedName("positionId") val positionId: String?
) : Parcelable