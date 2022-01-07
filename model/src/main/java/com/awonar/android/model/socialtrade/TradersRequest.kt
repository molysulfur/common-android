package com.awonar.android.model.socialtrade

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class TradersRequest(
    val filter: String? = null,
    val page: Int = 0,
    val uid: String? = null,
) : Parcelable