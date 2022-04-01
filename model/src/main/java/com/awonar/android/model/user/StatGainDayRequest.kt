package com.awonar.android.model.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StatGainDayRequest(
    val uid: String,
    val year: String,
) : Parcelable