package com.awonar.android.model.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Amount(
    var amount: Float,
    var unit: Float,
    var type: String
) : Parcelable