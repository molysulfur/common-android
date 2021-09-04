package com.awonar.android.model.settting

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Country(
    @SerializedName("id") val id: String?,
    @SerializedName("capital") val capital: String?,
    @SerializedName("continent") val continent: String?,
    @SerializedName("currency") val currency: String?,
    @SerializedName("currencyName") val currencyName: String?,
    @SerializedName("isoCode") val isoCode: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("phonePrefix") val phonePrefix: String?,
) : Parcelable