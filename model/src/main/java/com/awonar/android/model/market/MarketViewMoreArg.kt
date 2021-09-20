package com.awonar.android.model.market

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class MarketViewMoreArg(
    val instrumentType: String,
    val filterType: String
) : Parcelable