package com.awonar.android.model.profile

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class PublicExposureRequest(
    val username: String?,
    val category: String?,
) : Parcelable