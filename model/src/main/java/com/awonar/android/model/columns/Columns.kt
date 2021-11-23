package com.awonar.android.model.columns

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Columns(
    var columns: List<String>,
    var type: String
) : Parcelable