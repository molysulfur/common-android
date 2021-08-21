package com.awonar.android.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExistsEmailResponse(
    @SerializedName("message") val message: String,
    @SerializedName("code") val statusCode: Int,
) : Parcelable