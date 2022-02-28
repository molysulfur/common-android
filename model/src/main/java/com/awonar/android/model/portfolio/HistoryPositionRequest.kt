package com.awonar.android.model.portfolio

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HistoryPositionRequest(
    val page: Int,
    val time: Long,
    val symbol: String? = null,
) : Parcelable