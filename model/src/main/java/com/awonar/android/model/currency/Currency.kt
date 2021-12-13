package com.awonar.android.model.currency

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Currency(
    @SerializedName("conversionUsdRate") val conversionUsdRate: Float,
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("namePlural") val namePlural: String,
    @SerializedName("symbol") val symbol: String,
    @SerializedName("symbolNative") val symbolNative: String,
) : Parcelable