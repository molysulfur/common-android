package com.awonar.android.model.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CalAmountUnitRequest(
    val instrumentId: Int,
    val leverage: Int,
    val price: Float,
    var amount: Float,
) : Parcelable