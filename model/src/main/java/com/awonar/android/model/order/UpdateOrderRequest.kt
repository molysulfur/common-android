package com.awonar.android.model.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UpdateOrderRequest(
    val id: String,
    val stopLossRate: Float? = null,
    val takeProfitRate: Float? = null,
    val unitsToReduce: Float? = null
) : Parcelable