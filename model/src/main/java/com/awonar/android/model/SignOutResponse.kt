package com.awonar.android.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignOutResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String?
) : Parcelable