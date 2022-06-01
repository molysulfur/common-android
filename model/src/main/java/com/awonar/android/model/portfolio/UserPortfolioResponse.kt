package com.awonar.android.model.portfolio

import android.os.Parcelable
import com.awonar.android.model.market.Instrument
import com.awonar.android.model.user.User
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserPortfolioResponse(
    @SerializedName("copies") val copies: List<Copier>?,
    @SerializedName("entryOrder") val entries: List<EntryOrderPortfolio>?,
    @SerializedName("orders") val orders: List<OrderPortfolio>?,
    @SerializedName("positions") val positions: List<Position>?,
) : Parcelable

@Parcelize
data class Copier(
    @SerializedName("availableAmountinvested") val availableAmount: Float,
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
    @SerializedName("withdrawalSummary") val withdrawalSummary: Float,
    @SerializedName("parentUser") val user: UserCopier,
    @SerializedName("totalFees") var totalFees: Float,
    @SerializedName("units") var units: Float,
    @SerializedName("avgOpen") var avgOpen: Float = 0f,
    @SerializedName("invested") var invested: Float = 0f,
    @SerializedName("profitLoss") var profitLoss: Float = 0f,
    @SerializedName("profitLossPercent") var profitLossPercent: Float = 0f,
    @SerializedName("value") var value: Float = 0f,
    @SerializedName("fees") var fees: Float = 0f,
    @SerializedName("leverage") var leverage: Float = 0f,
    @SerializedName("current") var current: Float = 0f,
    @SerializedName("netInvested") var netInvested: Float = 0f,
    @SerializedName("netProfit") var netProfit: Float = 0f,
    @SerializedName("copyStopLoss") var copyStopLoss: Float = 0f,
    @SerializedName("copyStopLossPercent") var copyStopLossPercent: Float = 0f,
) : Parcelable

@Parcelize
data class UserCopier(
    @SerializedName("id") val id: String,
    @SerializedName("displayFullName") val isDisplayName: Boolean,
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
    @SerializedName("id") val id: String? = null,
    @SerializedName("ip") val ip: String? = null,
    @SerializedName("isBuy") val isBuy: Boolean = false,
    @SerializedName("type") val type: String? = null,
    @SerializedName("leverage") val leverage: Int = 0,
    @SerializedName("instrumentId") val instrumentId: Int = 0,
    @SerializedName("copyId") val copyId: String? = null,
    @SerializedName("netProfit") val netProfit: Float = 0f,
    @SerializedName("amount") val amount: Float = 0f,
    @SerializedName("exitOrder") val exitOrder: ExitOrder? = null,
    @SerializedName("exposure") val exposure: Float = 0f,
    @SerializedName("instrument") var instrument: Instrument? = null,
    @SerializedName("openDateTime") val openDateTime: String? = null,
    @SerializedName("openRate") var openRate: Float = 0f,
    @SerializedName("orderNo") val orderNo: String? = null,
    @SerializedName("parentPositionId") val parentPositionId: String? = null,
    @SerializedName("parentUid") val parentUid: String? = null,
    @SerializedName("parentUsername") val parentUsername: String? = null,
    @SerializedName("portfolioId") val portfolioId: String? = null,
    @SerializedName("positionNo") val positionNo: String? = null,
    @SerializedName("reason") val reason: String? = null,
    @SerializedName("closeType") val closeType: Int = 0,
    @SerializedName("stopLossRate") var stopLossRate: Float = 0f,
    @SerializedName("takeProfitRate") var takeProfitRate: Float = 0f,
    @SerializedName("totalFees") var totalFees: Float = 0f,
    @SerializedName("units") var units: Float = 0f,
    @SerializedName("closeRate") var closeRate: Float = 0f,
    @SerializedName("closeDateTime") var closeDateTime: String? = null,
    @SerializedName("conversionRate") var conversionRate: Float = 0f,
    @SerializedName("invested") var invested: Float = 0f,
    @SerializedName("open") var avgOpen: Float = 0f,
    @SerializedName("currentRate") var current: Float = 0f,
    @SerializedName("stopLoss") var stopLoss: Float = 0f,
    @SerializedName("takeProfit") var takeProfit: Float = 0f,
    @SerializedName("profitLoss") var profitLoss: Float = 0f,
    @SerializedName("profitLossPercent") var profitLossPercent: Float = 0f,
    @SerializedName("pipChange") var pipChange: Float = 0f,
    @SerializedName("value") var value: Float = 0f,
    @SerializedName("fees") var fees: Float = 0f,
    @SerializedName("amountStopLoss") var amountStopLoss: Float = 0f,
    @SerializedName("amountTakeProfit") var amountTakeProfit: Float = 0f,
    @SerializedName("stopLossPercent") var stopLossPercent: Float = 0f,
    @SerializedName("takeProfitPercent") var takeProfitPercent: Float = 0f,
    @SerializedName("change") var change: Float = 0f,
    @SerializedName("changePercent") var changePercent: Float = 0f,
    @SerializedName("status") var status: String? = null,
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