package com.awonar.android.model.copier

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UpdateCopy(
    @SerializedName("id") val id: String,
    @SerializedName("slPct") val stoploss: Float,
) : Parcelable
