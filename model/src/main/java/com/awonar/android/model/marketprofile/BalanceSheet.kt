package com.awonar.android.model.marketprofile

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BalanceSheet(
    @SerializedName("assets") val assets :Long,
    @SerializedName("currentAssets") val currentAssets :Long,
    @SerializedName("currentLiabilities") val currentLiabilities :Long,
    @SerializedName("equity") val equity :Long,
    @SerializedName("equityAttributableToNoncontrollingInterest") val equityAttributableToNoncontrollingInterest :Long,
    @SerializedName("equityAttributableToParent") val equityAttributableToParent :Long,
    @SerializedName("fixedAssets") val fixedAssets :Long,
    @SerializedName("liabilities") val liabilities :Long,
    @SerializedName("liabilitiesAndEquity") val liabilitiesAndEquity :Long,
    @SerializedName("noncurrentAssets") val noncurrentAssets :Long,
    @SerializedName("noncurrentLiabilities") val noncurrentLiabilities :Long,
    @SerializedName("otherThanFixedNoncurrentAssets") val otherThanFixedNoncurrentAssets :Long,
    @SerializedName("fiscalPeriod") val fiscalPeriod :String?,
    @SerializedName("fiscalYear") val fiscalYear :String?,
) :Parcelable