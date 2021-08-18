package com.awonar.android.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class SignInRequest(
    @SerializedName("username") val username: String?,
    @SerializedName("password") val password: String?
) : Parcelable