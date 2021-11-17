package com.awonar.android.model.history

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HistoryPaging(
    val history: List<History>,
    val page: Int
) : Parcelable