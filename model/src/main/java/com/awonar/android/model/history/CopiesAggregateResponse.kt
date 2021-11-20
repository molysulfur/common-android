package com.awonar.android.model.history

import android.os.Parcelable
import com.awonar.android.model.user.Master
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CopiesAggregateResponse(
    @SerializedName("aggregateCopy") val aggregateCopy: CopiesAggregate,
    @SerializedName("historyCopy") val historyCopy: List<CopiesHistory>?,
    @SerializedName("meta") val meta: HistoryMeta
) : Parcelable

@Parcelize
data class CopiesAggregate(
    @SerializedName("endEquity") val endEquity: Float,
    @SerializedName("master") val master: Master?,
    @SerializedName("totalFees") val totalFees: Float,
    @SerializedName("totalInvestment") val totalInvestment: Float,
) : Parcelable

@Parcelize
data class CopiesHistory(
    @SerializedName("id") var id: String?,
    @SerializedName("availableAmount") var availableAmount: Float,
    @SerializedName("closedPositionsNetProfit") var closedPositionsNetProfit: Float,
    @SerializedName("copyExistingPositions") var copyExistingPositions: Boolean,
    @SerializedName("copyId") var copyId: String?,
    @SerializedName("depositSummary") var depositSummary: Float,
    @SerializedName("endEquity") var endEquity: Float,
    @SerializedName("endedCopyDate") var endedCopyDate: String?,
    @SerializedName("initialInvestment") var initialInvestment: Float,
    @SerializedName("isPaused") var isPaused: Boolean,
    @SerializedName("master") var master: Master?,
    @SerializedName("parentUserId") var parentUserId: String?,
    @SerializedName("parentUsername") var parentUsername: String?,
    @SerializedName("startedCopyDate") var startedCopyDate: String?,
    @SerializedName("stopLossAmount") var stopLossAmount: Float,
    @SerializedName("stopLossPercentage") var stopLossPercentage: Float,
    @SerializedName("totalFees") var totalFees: Float,
    @SerializedName("totalInvestment") var totalInvestment: Float,
    @SerializedName("totalNetProfit") var totalNetProfit: Float,
    @SerializedName("withdrawalSummary") var withdrawalSummary: Float,
) : Parcelable
