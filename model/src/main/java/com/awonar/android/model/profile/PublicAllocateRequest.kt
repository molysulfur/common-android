package com.awonar.android.model.profile

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class PublicAllocateRequest(
    val username: String?,
    val category: String?,
) : Parcelable