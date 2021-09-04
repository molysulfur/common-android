package com.awonar.android.model.settting

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Bank(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("shortName") val shortName: String?,
) : Parcelable