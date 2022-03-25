package com.awonar.android.model.search

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchRequest(
    @SerializedName("keyword") val keyword: String,
    @SerializedName("page") val page: Int,
    @SerializedName("type") val type: String,
) : Parcelable {
}