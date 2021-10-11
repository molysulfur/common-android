package com.awonar.android.model.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ValidateStopLossRequest(
    val stopLoss: Price,
    val digit: Int,
    val openPrice: Float
) : Parcelable