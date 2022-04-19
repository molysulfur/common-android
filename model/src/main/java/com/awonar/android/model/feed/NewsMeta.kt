package com.awonar.android.model.feed

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewsMetaResponse(
    @SerializedName("metadata") val meta: NewsMeta?,
) : Parcelable

@Parcelize
data class NewsMeta(
    @SerializedName("description") val description: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("hostname") val hostname: String?,
    @SerializedName("siteName") val siteName: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("url") val url: String?,
) : Parcelable