package com.awonar.android.model.user

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FollowResponse(
    @SerializedName("isFollow") val isFollow: Boolean = false,
) : Parcelable