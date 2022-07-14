package com.awonar.android.model.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MinMaxStopLossRequest(
    val isBuy: Boolean,
    val amount: Float,
    val units: Float,
    val leverage: Int,
    val instrumentId: Int,
) : Parcelable