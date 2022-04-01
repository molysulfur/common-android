package com.awonar.android.model.portfolio

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HistoryPositionRequest(
    val page: Int = 0,
    val time: Long = 0L,
    val symbol: String? = null,
) : Parcelable