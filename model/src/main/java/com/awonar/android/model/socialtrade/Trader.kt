package com.awonar.android.model.socialtrade

import android.os.Parcelable
import com.awonar.android.model.user.Address
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Trader(
    @SerializedName("id") val id: String?,
    @SerializedName("licenseId") val licenseId: String?,
    @SerializedName("username") val username: String?,
    @SerializedName("picture") val image: String?,
    @SerializedName("firstName") val firstName: String?,
    @SerializedName("middleName") val middleName: String?,
    @SerializedName("lastName") val lastName: String?,
    @SerializedName("address") val address: Address?,
    @SerializedName("aumLv") val aumLv: Int,
    @SerializedName("verified") val verified: Boolean,
    @SerializedName("aumDescription") val aumDescription: String?,
    @SerializedName("displayFullName") val displayFullName: Boolean,
    @SerializedName("profitable") val profitable: Float,
    @SerializedName("gain") val gain: Float,
    @SerializedName("risk") val risk: Int,
    @SerializedName("maxRisk") val maxRisk: Int,
    @SerializedName("activeWeek") val activeWeek: Int,
    @SerializedName("totalTrade") val totalTrade: Int,
    @SerializedName("totalCopiers") val totalCopiers: Int,
    @SerializedName("dailyDrawdown") val dailyDrawdown: Float,
    @SerializedName("weeklyDrawdown") val weeklyDrawdown: Float,
    @SerializedName("yearlyDrawdown") val yearlyDrawdown: Float,
) : Parcelable

