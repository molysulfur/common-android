package com.awonar.android.model.search

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchResponse(
    @SerializedName("markets") val markets: List<Search>?,
    @SerializedName("people") val people: List<Search>?,
) : Parcelable