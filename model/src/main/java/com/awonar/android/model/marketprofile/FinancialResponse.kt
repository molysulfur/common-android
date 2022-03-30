package com.awonar.android.model.marketprofile

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FinancialResponse(
    @SerializedName("statistics") val ststistics: FinancialStatistic,
    @SerializedName("incomeStatement") val incomeStatement: IncomeStatement,
    @SerializedName("cashFlowStatement") val cashFlow: CashFlowStatement,
    @SerializedName("balanceSheet") val balanceSheet: BalanceSheetStatement,
) : Parcelable

@Parcelize
data class BalanceSheetStatement(
    @SerializedName("quarter") val quarter: List<Map<String, String?>>,
    @SerializedName("year") val year: List<Map<String, String?>>,
) : Parcelable


@Parcelize
data class CashFlowStatement(
    @SerializedName("quarter") val quarter: List<Map<String, String?>>,
    @SerializedName("year") val year: List<Map<String, String?>>,
) : Parcelable

@Parcelize
data class IncomeStatement(
    @SerializedName("quarter") val quarter: List<Map<String, String?>>,
    @SerializedName("year") val year: List<Map<String, String?>>,
) : Parcelable

@Parcelize
data class FinancialStatistic(
    @SerializedName("dividend") val dividend: DividendResponse,
    @SerializedName("split") val split: SplitResponse,
    @SerializedName("quarter") val quarter: List<FinancialQuarter>,
    @SerializedName("year") val year: List<FinancialQuarter>,
) : Parcelable

@Parcelize
data class FinancialQuarter(
    @SerializedName("grossMargin") val grossMargin: String?,
    @SerializedName("operatingMargin") val operatingMargin: Float,
    @SerializedName("currentRatio") val currentRatio: Float,
    @SerializedName("operatingCashFlow") val operatingCashFlow: Float,
    @SerializedName("fiscalPeriod") val fiscalPeriod: String?,
    @SerializedName("fiscalYear") val fiscalYear: Long,
) : Parcelable

@Parcelize
data class SplitResponse(
    @SerializedName("historical") val historical: List<SplitHistory>,
) : Parcelable

@Parcelize
data class SplitHistory(
    @SerializedName("date") val date: String?,
    @SerializedName("label") val label: String?,
    @SerializedName("numerator") val numerator: Int,
    @SerializedName("denominator") val denominator: Int,
) : Parcelable

@Parcelize
data class DividendResponse(
    @SerializedName("historical") val historical: List<DividendHistory>,
) : Parcelable

@Parcelize
data class DividendHistory(
    @SerializedName("date") val date: String?,
    @SerializedName("label") val label: String?,
    @SerializedName("recordDate") val recordDate: String?,
    @SerializedName("paymentDate") val paymentDate: String?,
    @SerializedName("declarationDate") val declarationDate: String?,
    @SerializedName("adjDividend") val adjDividend: Float,
    @SerializedName("dividend") val dividend: Float,
) : Parcelable