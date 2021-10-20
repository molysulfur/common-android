package com.awonar.android.model.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OvernightFeeRequest(
    val instrumentId: Int,
    val amount: Price,
    val leverage: Int,
    val orderType :String
) : Parcelable