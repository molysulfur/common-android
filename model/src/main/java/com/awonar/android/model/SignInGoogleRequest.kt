package com.awonar.android.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignInGoogleRequest(
    @SerializedName("email") val email: String?,
    @SerializedName("googleId") val id: String?,
    @SerializedName("googleToken") val token: String?,
) : Parcelable