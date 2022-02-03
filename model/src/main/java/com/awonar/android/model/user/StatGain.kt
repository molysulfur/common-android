package com.awonar.android.model.user

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class StatGain(
    @SerializedName("date") val date: String?,
    @SerializedName("gain") val gain: Float,
) : Parcelable