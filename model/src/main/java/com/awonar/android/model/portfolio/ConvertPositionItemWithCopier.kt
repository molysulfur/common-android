package com.awonar.android.model.portfolio

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ConvertPositionItemWithCopier(
    val instrumentFilterId: Int,
    val positions: List<Position>
) : Parcelable