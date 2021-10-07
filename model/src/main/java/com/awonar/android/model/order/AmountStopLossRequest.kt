package com.awonar.android.model.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AmountStopLossRequest(
    val instrumentId: Int,
    val stopLoss: Float,
    val openPrice: Float,
    val unit: Float
) : Parcelable