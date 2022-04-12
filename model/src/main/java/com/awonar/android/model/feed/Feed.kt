package com.awonar.android.model.feed

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Feed(
    @SerializedName("commentTotal") val countComment: Int,
    @SerializedName("comment") val comments: List<Comment>?,
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("id") val id: String?,
    @SerializedName("images") val images: List<String?>?,
    @SerializedName("instruments") val instruments: Int?,
    @SerializedName("isLike") val isLike: Boolean,
    @SerializedName("likeTotal") val countLike: Int,
    @SerializedName("links") val links: List<String?>?,
    @SerializedName("newsId") val newsId: String?,
    @SerializedName("sharePostData") val sharePostData: String?,
    @SerializedName("shareTotal") val shareTotal: Int,
    @SerializedName("status") val status: String?,
    @SerializedName("tagsUsers") val tagsUsers: List<String?>?,
    @SerializedName("type") val type: String?,
    @SerializedName("updatedAt") val updatedAt: String?,
) : Parcelable
