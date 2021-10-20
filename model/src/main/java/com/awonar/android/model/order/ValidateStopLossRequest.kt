package com.awonar.android.model.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ValidateStopLossRequest(
    val instrumentId: Int,
    val stopLoss: Price,
    val amount: Float,
    val digit: Int,
    val openPrice: Float,
) : Parcelable