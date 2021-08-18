package com.awonar.android.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class SignInRequest(
    val username: String?,
    val password: String?
) : Parcelable