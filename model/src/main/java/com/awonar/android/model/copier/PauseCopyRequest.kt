package com.awonar.android.model.copier

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PauseCopyRequest(
    @SerializedName("id") val copyId: String,
    @SerializedName("pauseCopy") val isPause: Boolean
) : Parcelable