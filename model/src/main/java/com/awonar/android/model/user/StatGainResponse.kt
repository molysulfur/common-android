package com.awonar.android.model.user

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class StatGainResponse(
    @SerializedName("months") val months: List<StatGain>,
    @SerializedName("years") val years: List<StatGain>,
) : Parcelable