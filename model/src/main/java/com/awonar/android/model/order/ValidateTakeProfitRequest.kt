package com.awonar.android.model.order

import android.os.Parcelable
import com.awonar.android.model.market.Instrument
import com.awonar.android.model.market.Quote
import kotlinx.parcelize.Parcelize

@Parcelize
data class ValidateRateTakeProfitRequest(
    val amount: Float,
    val rateTp: Float,
    val currentPrice: Float,
    val openPrice: Float,
    val units: Float,
    val isBuy: Boolean = false,
    val conversionRate: Float,
    val maxTakeProfitPercentage: Float,
    val digit: Int,
    val quote : Quote
) : Parcelable

@Parcelize
data class ValidateRateStopLossRequest(
    val rateSl: Float,
    val amountSl: Float,
    val openPrice: Float,
    val amount: Float,
    val exposure: Float,
    val units: Float,
    val leverage: Int,
    val isBuy: Boolean = false,
    val available: Float,
    val conversionRate: Float,
    val maxStopLoss: Float,
    val digit: Int,
    val quote: Quote
) : Parcelable


@Parcelize
data class ValidateAmountTakeProfitRequest(
    val instrumentId: Int,
    val stopLoss: Price
) : Parcelable