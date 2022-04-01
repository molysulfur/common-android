package com.awonar.android.model.profile

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PublicExposure(
  @SerializedName("name") val name :String?,
  @SerializedName("value") val value :Float,
) : Parcelable