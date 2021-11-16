package com.awonar.android.model.portfolio

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PieChartRequest(
    val data: Map<String, Double>,
    val hasViewAll: Boolean
) : Parcelable