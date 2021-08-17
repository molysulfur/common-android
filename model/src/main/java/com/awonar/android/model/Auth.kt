package com.awonar.android.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Auth(
    val accessToken: String?,
    val expiresIn: Int,
    val refreshToken: String?
) : Parcelable {
    fun isExpireToken(): Boolean = System.currentTimeMillis() >= expiresIn
}