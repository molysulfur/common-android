package com.awonar.android.model.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class ExposureRequest(
    val instrumentId: Int,
    val amount: Float,
    val leverage: Int
) : Parcelable {
    override fun toString(): String {
        return "instrumentId: $instrumentId,amount: $amount,leverage: $leverage"
    }
}