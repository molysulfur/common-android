package com.awonar.android.model.search

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Search(
    @SerializedName("id") val id: String?,
    @SerializedName("keyword") val keyword: String?,
    @SerializedName("refId") val refId: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("userId") val userId: String?,
    @SerializedName("data") val data: SearchData?,
) : Parcelable

@Parcelize
data class SearchData(
    @SerializedName("id") val id: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("picture") val picture: String?,
    @SerializedName("isFollow") var isFollow: Boolean = false,
) : Parcelable

