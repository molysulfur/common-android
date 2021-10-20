package com.awonar.android.model.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RateStopLossRequest(
    val instrumentId: Int,
    val amountStopLoss: Float,
    val openPrice: Float,
    val unit: Float
) : Parcelable
