package com.awonar.android.model.history

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HistoryRequest(
    val timestamp: Long,
    val page: Int = 1,
    val filter: String? = null,
    val symbol: String? = null,
    val copyId: String? = null,
    val username: String? = null
) : Parcelable