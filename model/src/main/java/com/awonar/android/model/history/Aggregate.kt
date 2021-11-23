package com.awonar.android.model.history

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Aggregate(
    @SerializedName("endEquity") val endEquity: Float,
    @SerializedName("startEquity") val startEquity: Float,
    @SerializedName("totalCompensation") val totalCompensation: Float,
    @SerializedName("totalDeduct") val totalDeduct: Float,
    @SerializedName("totalDeposit") val totalDeposit: Float,
    @SerializedName("totalFees") val totalFees: Float,
    @SerializedName("totalMoneyIn") val totalMoneyIn: Float,
    @SerializedName("totalMoneyOut") val totalMoneyOut: Float,
    @SerializedName("totalNetProfit") val totalNetProfit: Float,
    @SerializedName("totalRefund") val totalRefund: Float,
    @SerializedName("totalOthers") val totalOthers: Float,
    @SerializedName("totalWithdrawal") val totalWithdrawal: Float,
    @SerializedName("totalWithdrawalFees") val totalWithdrawalFees: Float,
    @SerializedName("portfolioId") val portfolioId: String,
    @SerializedName("summaryDate") val summaryDate: String,
    @SerializedName("summaryTimestamp") val summaryTimestamp: Int,
    @SerializedName("id") val id: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("updatedAt") val updatedAt: String?
) : Parcelable