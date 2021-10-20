package com.awonar.android.model.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StopLossRequest(
    val instrumentId: Int,
    val stopLoss: Price,
    val openPrice: Float,
    val unit: Float
) : Parcelable