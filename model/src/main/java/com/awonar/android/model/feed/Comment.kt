package com.awonar.android.model.feed

import android.os.Parcelable
import com.awonar.android.model.user.User
import com.awonar.android.model.user.UserResponse
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Comment(
    @SerializedName("id") val id: String?,
    @SerializedName("postId") val postId: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("instruments") val instruments: Int?,
    @SerializedName("isLike") val isLike: Boolean,
    @SerializedName("likeTotal") val countLike: Int,
    @SerializedName("links") val links: String?,
    @SerializedName("subComment") val reply: List<Comment>,
    @SerializedName("tagsUser") val tagsUser: List<String?>?,
    @SerializedName("updatedAt") val updatedAt: String?,
    @SerializedName("user") val user: UserResponse?,
    @SerializedName("userId") val userId: String?,
) : Parcelable
