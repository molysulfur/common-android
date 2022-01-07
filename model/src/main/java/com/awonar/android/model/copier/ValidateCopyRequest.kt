package com.awonar.android.model.copier

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ValidateCopyRequest(
    val amount: Float,
    val stopLoss: Float
) : Parcelable