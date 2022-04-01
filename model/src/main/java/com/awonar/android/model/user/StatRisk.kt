package com.awonar.android.model.user

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class StatRisk(
    @SerializedName("risk") val risk: Int,
    @SerializedName("maxRisk") val maxRisk: Int,
    @SerializedName("date") val date: String?,
) : Parcelable
