package com.awonar.android.model

import android.os.Parcelable
import android.util.Log
import kotlinx.parcelize.Parcelize

@Parcelize
data class Auth(
    val accessToken: String?,
    val expiresIn: Int,
    val refreshToken: String?
) : Parcelable {
    fun isExpireToken(): Boolean = System.currentTimeMillis().div(1000) >= expiresIn
}