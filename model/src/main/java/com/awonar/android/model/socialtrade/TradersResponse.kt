package com.awonar.android.model.socialtrade

import android.os.Parcelable
import com.awonar.android.model.core.Meta
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TradersResponse(
    @SerializedName("traders") val traders: List<Trader>,
    @SerializedName("meta") val meta: Meta
) : Parcelable