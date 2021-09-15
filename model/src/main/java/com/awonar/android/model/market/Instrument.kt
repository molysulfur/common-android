package com.awonar.android.model.market

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class InstrumentResponse(
    @SerializedName("instruments") val instruments: List<Instrument>
) : Parcelable

@Parcelize
data class Instrument(
    @SerializedName("id") val id: Int,
    @SerializedName("exchangeId") val exchangeId: String?,
    @SerializedName("symbol") val symbol: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("companyName") val companyName: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("sector") val sector: String?,
    @SerializedName("assetBase") val base: String?,
    @SerializedName("assetQuote") val quote: String?,
    @SerializedName("digit") val digit: String?,
    @SerializedName("categories") val categories: List<String>?,
    @SerializedName("picture") val logo: String?,
    @SerializedName("industry") val industry: String?,
    @SerializedName("recommend") val recommend: Boolean,
) : Parcelable

