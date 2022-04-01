package com.awonar.android.model.marketprofile

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Income(
    @SerializedName("benefitsCostsExpenses") val benefitsCostsExpenses: Long,
    @SerializedName("costOfRevenue") val costOfRevenue: Long,
    @SerializedName("costsAndExpenses") val costsAndExpenses: Long,
    @SerializedName("grossProfit") val grossProfit: Long,
    @SerializedName("incomeTaxExpenseBenefit") val incomeTaxExpenseBenefit: Long,
    @SerializedName("incomeTaxExpenseBenefitDeferred") val incomeTaxExpenseBenefitDeferred: Long,
    @SerializedName("incomeLossFromContinuingOperationsAfterTax") val incomeLossFromContinuingOperationsAfterTax: Long,
    @SerializedName("incomeLossFromContinuingOperationsBeforeTax") val incomeLossFromContinuingOperationsBeforeTax: Long,
    @SerializedName("interestExpenseOperating") val interestExpenseOperating: Long,
    @SerializedName("netIncomeLoss") val netIncomeLoss: Long,
    @SerializedName("netIncomeLossAttributableToNoncontrollingInterest") val netIncomeLossAttributableToNoncontrollingInterest: Long,
    @SerializedName("netIncomeLossAttributableToParent") val netIncomeLossAttributableToParent: Long,
    @SerializedName("netIncomeLossAvailableToCommonStockholdersBasic") val netIncomeLossAvailableToCommonStockholdersBasic: Long,
    @SerializedName("nonoperatingIncomeLoss") val nonoperatingIncomeLoss: Long,
    @SerializedName("operatingExpenses") val operatingExpenses: Long,
    @SerializedName("operatingIncomeLoss") val operatingIncomeLoss: Long,
    @SerializedName("participatingSecuritiesDistributedAndUndistributedEarningsLossBasic") val participatingSecuritiesDistributedAndUndistributedEarningsLossBasic: Long,
    @SerializedName("preferredStockDividendsAndOtherAdjustments") val preferredStockDividendsAndOtherAdjustments: Long,
    @SerializedName("revenues") val revenues: Long,
    @SerializedName("fiscalPeriod") val fiscalPeriod: String?,
    @SerializedName("fiscalYear") val fiscalYear: String?,
) : Parcelable