package com.awonar.android.model.feed

import android.os.Parcelable
import com.awonar.android.model.user.User
import com.awonar.android.model.user.UserResponse
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Feed(
    @SerializedName("id") val id: String?,
    @SerializedName("likeTotal") val countLike: Int,
    @SerializedName("commentTotal") val countComment: Int,
    @SerializedName("isLike") val isLike: Boolean,
    @SerializedName("shareTotal") val shareTotal: Int,
    @SerializedName("sharePostData") val sharePostData: SharedFeed?,
    @SerializedName("tagsUsers") val tagsUsers: List<String?>?,
    @SerializedName("links") val links: List<String?>?,
    @SerializedName("description") val description: String?,
    @SerializedName("images") val images: List<String?>?,
    @SerializedName("instruments") val instruments: List<InstrumentsFeed>?,
    @SerializedName("user") val user: UserResponse?,
    @SerializedName("comments") val comments: List<Comment>?,
    @SerializedName("newsId") val newsId: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("updatedAt") val updatedAt: String?,
) : Parcelable

@Parcelize
data class InstrumentsFeed(
    @SerializedName("id") val id: Int,
    @SerializedName("stampPrice") val price: Float,
    @SerializedName("stampSymbol") val stampSymbol: String?,
) : Parcelable

@Parcelize
data class SharedFeed(
    @SerializedName("id") val id: String?,
    @SerializedName("links") val links: List<String>?,
    @SerializedName("description") val description: String?,
    @SerializedName("images") val images: List<String>?,
    @SerializedName("instruments") val instruments: List<InstrumentsFeed>?,
    @SerializedName("user") val user: UserResponse?,
    @SerializedName("type") val type: String?,
    @SerializedName("createdAt") val createdAt: String?,
) : Parcelable
