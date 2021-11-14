package com.awonar.android.model.portfolio

import android.os.Parcelable
import com.awonar.android.model.market.Instrument
import com.awonar.android.model.user.User
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserPortfolioResponse(
    @SerializedName("copies") val copies: List<Copier>,
    @SerializedName("entryOrder") val entries: List<EntryOrderPortfolio>,
    @SerializedName("orders") val orders: List<OrderPortfolio>,
    @SerializedName("positions") val positions: List<Position>
) : Parcelable

@Parcelize
data class Copier(
    @SerializedName("availableAmount") val availableAmount: Float,
    @SerializedName("closedPositionsNetProfit") val closedPositionsNetProfit: Float,
    @SerializedName("copyExistingPositions") val copyExistingPositions: Boolean,
    @SerializedName("depositSummary") val depositSummary: Float,
    @SerializedName("entryOrder") val entryOrder: List<EntryOrderPortfolio>?,
    @SerializedName("historyPositions") val historyPositions: List<Position>?,
    @SerializedName("id") val id: String,
    @SerializedName("initialInvestment") val initialInvestment: Float,
    @SerializedName("investAmount") val investAmount: Float,
    @SerializedName("isPaused") val isPaused: Boolean,
    @SerializedName("orders") val orders: List<OrderPortfolio>?,
    @SerializedName("parentUserId") val parentUserId: String,
    @SerializedName("parentUsername") val parentUsername: String,
    @SerializedName("pendingForClosure") val pendingForClosure: Boolean,
    @SerializedName("positions") val positions: List<Position>?,
    @SerializedName("startedCopyDate") val startedCopyDate: String,
    @SerializedName("stopLossAmount") val stopLossAmount: Float,
    @SerializedName("stopLossPercentage") val stopLossPercentage: Float,
    @SerializedName("totalFees") val totalFees: Float,
    @SerializedName("withdrawalSummary") val withdrawalSummary: Float,
    @SerializedName("parentUser") val user: UserCopier
) : Parcelable

@Parcelize
data class UserCopier(
    @SerializedName("id") val id: String,
    @SerializedName("displayFullName") val fullName: String?,
    @SerializedName("firstName") val firstName: String?,
    @SerializedName("lastName") val lastName: String?,
    @SerializedName("middleName") val middleName: String?,
    @SerializedName("picture") val picture: String?,
    @SerializedName("private") val private: Boolean,
    @SerializedName("username") val username: String?,
) : Parcelable

@Parcelize
data class EntryOrderPortfolio(
    @SerializedName("id") val id: String,
    @SerializedName("instrumentId") val instrumentId: Int,
    @SerializedName("copyId") val copyId: String,
    @SerializedName("amount") val amount: Float,
    @SerializedName("exposure") val exposure: Float,
    @SerializedName("initOpenRate") val initOpenRate: Float,
    @SerializedName("ip") val ip: String,
    @SerializedName("isBuy") val isBuy: Boolean,
    @SerializedName("leverage") val leverage: Int,
    @SerializedName("openDateTime") val openDateTime: String?,
    @SerializedName("orderNo") val orderNo: String,
    @SerializedName("parentPositionId") val parentPositionId: String,
    @SerializedName("parentUid") val parentUid: String,
    @SerializedName("parentUsername") val parentUsername: String,
    @SerializedName("reason") val reason: String,
    @SerializedName("stopLossPercentage") val stopLossPercentage: Float,
    @SerializedName("takeProfitPercentage") val takeProfitPercentage: Float,
) : Parcelable

@Parcelize
data class OrderPortfolio(
    @SerializedName("id") val id: String,
    @SerializedName("instrumentId") val instrumentId: Int,
    @SerializedName("copyId") val copyId: String,
    @SerializedName("amount") val amount: Float,
    @SerializedName("exposure") val exposure: Float,
    @SerializedName("ip") val ip: String,
    @SerializedName("isBuy") val isBuy: Boolean,
    @SerializedName("leverage") val leverage: Int,
    @SerializedName("openDateTime") val openDateTime: String?,
    @SerializedName("orderNo") val orderNo: String,
    @SerializedName("parentOrderId") val parentOrderId: String,
    @SerializedName("rate") val rate: Float,
    @SerializedName("reason") val reason: String,
    @SerializedName("stopLossRate") val stopLossRate: Float,
    @SerializedName("takeProfitRate") val takeProfitRate: Float,
    @SerializedName("units") val units: Float,
) : Parcelable

@Parcelize
data class Position(
    @SerializedName("id") val id: String,
    @SerializedName("ip") val ip: String,
    @SerializedName("isBuy") val isBuy: Boolean,
    @SerializedName("leverage") val leverage: Int,
    @SerializedName("netProfit") val netProfit: Float,
    @SerializedName("instrumentId") val instrumentId: Int,
    @SerializedName("copyId") val copyId: String,
    @SerializedName("amount") val amount: Float,
    @SerializedName("exitOrder") val exitOrder: ExitOrder?,
    @SerializedName("exposure") val exposure: Float,
    @SerializedName("instrument") val instrument: Instrument,
    @SerializedName("openDateTime") val openDateTime: String?,
    @SerializedName("openRate") val openRate: Float,
    @SerializedName("orderNo") val orderNo: String,
    @SerializedName("parentPositionId") val parentPositionId: String,
    @SerializedName("parentUid") val parentUid: String,
    @SerializedName("parentUsername") val parentUsername: String,
    @SerializedName("portfolioId") val portfolioId: String,
    @SerializedName("positionNo") val positionNo: String,
    @SerializedName("reason") val reason: String,
    @SerializedName("stopLossRate") val stopLossRate: Float,
    @SerializedName("takeProfitRate") val takeProfitRate: Float,
    @SerializedName("totalFees") val totalFees: Float,
    @SerializedName("units") val units: Float,
    @SerializedName("closeRate") val closeRate: Float,
    @SerializedName("closeType") val closeType: Int,
    @SerializedName("closeDateTime") val closeDateTime: String?,
) : Parcelable


@Parcelize
data class ExitOrder(
    @SerializedName("id") val id: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("ip") val ip: String,
    @SerializedName("isManagerAction") val isManagerAction: Boolean,
    @SerializedName("portfolioId") val portfolioId: String,
    @SerializedName("positionId") val positionId: String,
    @SerializedName("reason") val reason: String,
    @SerializedName("unitsToDeduce") val unitsToDeduce: Float,
) : Parcelable