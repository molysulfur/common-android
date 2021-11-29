package com.awonar.android.model.order

import android.os.Parcelable
import com.awonar.android.model.market.Instrument
import kotlinx.parcelize.Parcelize

@Parcelize
data class ValidateRateTakeProfitRequest(
    val rateTp: Float,
    val currentPrice: Float,
    val openPrice: Float,
    val value: Float,
    val units: Float,
    val isBuy: Boolean = false,
    val instrument: Instrument
) : Parcelable

@Parcelize
data class ValidateRateStopLossRequest(
    val rateSl: Float,
    val amountSl :Float,
    val currentPrice: Float,
    val openPrice: Float,
    val amount :Float,
    val exposure :Float,
    val units: Float,
    val leverage: Int,
    val isBuy: Boolean = false,
    val instrument: Instrument,
    val available : Float,
) : Parcelable


@Parcelize
data class ValidateAmountTakeProfitRequest(
    val instrumentId: Int,
    val stopLoss: Price
) : Parcelable