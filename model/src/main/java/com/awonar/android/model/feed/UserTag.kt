package com.awonar.android.model.feed

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserTag(
    @SerializedName("id") val id: String?,
    @SerializedName("username") val username: String?,
) : Parcelable