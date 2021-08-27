package com.awonar.android.model.user

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UpdateAboutMeRequest(
    @SerializedName("about") val about: String?,
    @SerializedName("bio") val bio: String?,
    @SerializedName("facebook") val facebook: String?,
    @SerializedName("investmentSkills") val investmentSkills: String?,
    @SerializedName("linkedin") val linkedin: String?,
    @SerializedName("twitter") val twitter: String?,
    @SerializedName("website") val website: String?,
    @SerializedName("youtube") val youtube: String?,
) : Parcelable