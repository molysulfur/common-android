package com.awonar.android.model.marketprofile

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CashFlow(
    @SerializedName("netCashFlow") val netCashFlow: Long,
    @SerializedName("netCashFlowFromFinancingActivities") val netCashFlowFromFinancingActivities: Long,
    @SerializedName("netCashFlowFromFinancingActivitiesContinuing") val netCashFlowFromFinancingActivitiesContinuing: Long,
    @SerializedName("netCashFlowFromInvestingActivities") val netCashFlowFromInvestingActivities: Long,
    @SerializedName("netCashFlowFromInvestingActivitiesContinuing") val netCashFlowFromInvestingActivitiesContinuing: Long,
    @SerializedName("netCashFlowFromOperatingActivities") val netCashFlowFromOperatingActivities: Long,
    @SerializedName("netCashFlowFromOperatingActivitiesContinuing") val netCashFlowFromOperatingActivitiesContinuing: Long,
    @SerializedName("fiscalPeriod") val fiscalPeriod: String?,
    @SerializedName("fiscalYear") val fiscalYear: String?,
) : Parcelable
