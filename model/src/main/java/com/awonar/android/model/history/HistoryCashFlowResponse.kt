package com.awonar.android.model.history

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class HistoryCashFlow(
    val transactionNo: String?,
    val createdAt: String?,
    val cashflow: CashFlow?,
    val transactionDate: String?,
    val transactionType: Int
) : Parcelable

@Parcelize
data class HistoryCashFlowResponse(
    @SerializedName("TransactionNo") val transactionNo: String?,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("result") val cashflow: List<CashFlow>,
    @SerializedName("transactionDate") val transactionDate: String,
    @SerializedName("transactionType") val transactionType: Int
) : Parcelable

@Parcelize
data class CashFlow(
    @SerializedName("id") val id: String?,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("depositNo") val depositNo: Int,
    @SerializedName("withdrawNo") val withdrawNo: Int,
    @SerializedName("dollarAmount") val dollarAmount: Float,
    @SerializedName("fee") val fee: Float,
    @SerializedName("localAmount") val localAmount: Float?,
    @SerializedName("localCurrencyId") val localCurrencyId: String?,
    @SerializedName("localRate") val localRate: Float?,
    @SerializedName("status") val status: String?,
) : Parcelable