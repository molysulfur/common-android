package com.awonar.android.model.core

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MessageSuccessResponse(
    @SerializedName("code") val statusCode: Int,
    @SerializedName("message") val message: String,
) : Parcelable