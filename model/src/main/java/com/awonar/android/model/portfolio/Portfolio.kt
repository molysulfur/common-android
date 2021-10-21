package com.awonar.android.model.portfolio

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Portfolio(
    @SerializedName("id") val id: String,
    @SerializedName("active") val active: Boolean,
    @SerializedName("available") val available: Float,
    @SerializedName("currencyId") val currencyId: String,
    @SerializedName("totalAllocated") val totalAllocated: Float,
    @SerializedName("type") val type: String,
    @SerializedName("userId") val userId: String,
) : Parcelable
