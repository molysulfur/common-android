package com.awonar.android.model.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ValidateRateTakeProfitRequest(
    val takeProfit: Price,
    val openPrice: Float
) : Parcelable


@Parcelize
data class ValidateAmountTakeProfitRequest(
    val instrumentId: Int,
    val stopLoss: Price
) : Parcelable