package com.awonar.android.model.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MinMaxTakeProfitRequest(
    val isBuy: Boolean,
    val amount: Float,
    val units: Float,
    val openRate: Float,
    val leverage: Int,
    val instrumentId: Int,
) : Parcelable