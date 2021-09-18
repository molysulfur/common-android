package com.awonar.android.model.market

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class InstrumentProfile(
    @SerializedName("id") val id: Int,
    @SerializedName("symbol") val symbol: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("companyName") val companyName: String?,
    @SerializedName("exchangeId") val exchangeId: String?,
    @SerializedName("industry") val industry: String?,
    @SerializedName("website") val website: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("shortDescription") val shortDescription: String?,
    @SerializedName("ceo") val ceo: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("sector") val sector: String?,
    @SerializedName("employees") val employees: Int,
    @SerializedName("tags") val tags: List<String>,
    @SerializedName("address") val address: String?,
    @SerializedName("address2") val otherAddress: String?,
    @SerializedName("state") val state: String?,
    @SerializedName("city") val city: String?,
    @SerializedName("zip") val zip: String?,
    @SerializedName("country") val country: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("currency") val currency: String?,
    @SerializedName("picture") val picture: String?,
    @SerializedName("assetBase") val assetBase: String?,
    @SerializedName("assetQuote") val assetQuote: String?,
    @SerializedName("thumbnail") val thumbnail: String?,
    @SerializedName("peers") val peers: List<String?>,
    @SerializedName("active") val active: Boolean,
    @SerializedName("digit") val digit: Int,
    @SerializedName("recommend") val recommend: Boolean,
    @SerializedName("categories") val categories: List<String?>,
) : Parcelable