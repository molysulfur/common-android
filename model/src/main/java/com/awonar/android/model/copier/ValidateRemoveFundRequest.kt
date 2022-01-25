package com.awonar.android.model.copier

import android.os.Parcelable
import com.awonar.android.model.portfolio.Copier
import kotlinx.parcelize.Parcelize

@Parcelize
data class ValidateRemoveFundRequest(
    val copier: Copier,
    val amount: Float
) : Parcelable