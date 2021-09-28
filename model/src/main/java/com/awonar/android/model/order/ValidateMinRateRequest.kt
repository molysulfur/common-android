package com.awonar.android.model.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ValidateMinRateRequest(
    val rate: Float,
    val currentRate: Float
) : Parcelable