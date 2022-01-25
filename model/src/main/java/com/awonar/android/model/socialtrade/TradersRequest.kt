package com.awonar.android.model.socialtrade

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import retrofit2.http.Query


@Parcelize
data class TradersRequest(
    var filter: Map<String, String>? = null,
    var page: Int = 1,
    var limit: Int = 5,
) : Parcelable {
}

