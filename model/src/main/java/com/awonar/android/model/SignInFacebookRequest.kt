package com.awonar.android.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignInFacebookRequest(
    @SerializedName("email") val email: String? = null,
    @SerializedName("facebookId") val id: String? = null,
    @SerializedName("facebookToken") val token: String? = null,
) : Parcelable