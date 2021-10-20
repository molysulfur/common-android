package com.awonar.android.model.tradingdata

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "trading_data")
@Parcelize
data class TradingData(
    @PrimaryKey
    @ColumnInfo(name = "instrumentId") val instrumentId: Int,
    @ColumnInfo(name = "allowBuy") val allowBuy: Boolean,
    @ColumnInfo(name = "allowSell") val allowSell: Boolean,
    @ColumnInfo(name = "allowEditStopLoss") val allowEditStopLoss: Boolean,
    @ColumnInfo(name = "allowEditTakeProfit") val allowEditTakeProfit: Boolean,
    @ColumnInfo(name = "allowEditStopLossLeveraged") val allowEditStopLossLeveraged: Boolean,
    @ColumnInfo(name = "allowEditTakeProfitLeveraged") val allowEditTakeProfitLeveraged: Boolean,
    @ColumnInfo(name = "allowClosePosition") val allowClosePosition: Boolean,
    @ColumnInfo(name = "allowPartialClosePosition") val allowPartialClosePosition: Boolean,
    @ColumnInfo(name = "defaultLeverage") val defaultLeverage: Int,
    @ColumnInfo(name = "minLeverage") val minLeverage: Int,
    @ColumnInfo(name = "minPositionExposure") val minPositionExposure: Int,
    @ColumnInfo(name = "maxPositionExposure") val maxPositionExposure: Int,
    @ColumnInfo(name = "minPositionAmount") val minPositionAmount: Int,
    @ColumnInfo(name = "maxPositionAmount") val maxPositionAmount: Int,
    @ColumnInfo(name = "minTakeProfitPercentage") val minTakeProfitPercentage: Float,
    @ColumnInfo(name = "maxTakeProfitPercentage") val maxTakeProfitPercentage: Float,
    @ColumnInfo(name = "minStopLossPercentage") val minStopLossPercentage: Float,
    @ColumnInfo(name = "maxStopLossPercentageLeveragedBuy") val maxStopLossPercentageLeveragedBuy: Float,
    @ColumnInfo(name = "maxStopLossPercentageLeveragedSell") val maxStopLossPercentageLeveragedSell: Float,
    @ColumnInfo(name = "maxStopLossPercentageNonLeveragedBuy") val maxStopLossPercentageNonLeveragedBuy: Float,
    @ColumnInfo(name = "maxStopLossPercentageNonLeveragedSell") val maxStopLossPercentageNonLeveragedSell: Float,
    @ColumnInfo(name = "defaultStopLossPercentage") val defaultStopLossPercentage: Float,
    @ColumnInfo(name = "defaultStopLossPercentageLeveraged") val defaultStopLossPercentageLeveraged: Float,
    @ColumnInfo(name = "defaultStopLossPercentageNonLeveraged") val defaultStopLossPercentageNonLeveraged: Float,
    @ColumnInfo(name = "defaultTakeProfitPercentage") val defaultTakeProfitPercentage: Float,
    @ColumnInfo(name = "leveragedBuyEndOfWeekFee") val leveragedBuyEndOfWeekFee: Float,
    @ColumnInfo(name = "leveragedBuyOverNightFee") val leveragedBuyOverNightFee: Float,
    @ColumnInfo(name = "leveragedSellEndOfWeekFee") val leveragedSellEndOfWeekFee: Float,
    @ColumnInfo(name = "leveragedSellOverNightFee") val leveragedSellOverNightFee: Float,
    @ColumnInfo(name = "nonLeveragedBuyEndOfWeekFee") val nonLeveragedBuyEndOfWeekFee: Float,
    @ColumnInfo(name = "nonLeveragedBuyOverNightFee") val nonLeveragedBuyOverNightFee: Float,
    @ColumnInfo(name = "nonLeveragedSellEndOfWeekFee") val nonLeveragedSellEndOfWeekFee: Float,
    @ColumnInfo(name = "nonLeveragedSellOverNightFee") val nonLeveragedSellOverNightFee: Float,
    @ColumnInfo(name = "leverages") val leverages: List<String>,
) : Parcelable
