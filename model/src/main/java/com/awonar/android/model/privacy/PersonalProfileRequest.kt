package com.awonar.android.model.privacy

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PersonalProfileRequest(
    @SerializedName("birthDate") val birthDate: String?,
    @SerializedName("firstName") val firstName: String?,
    @SerializedName("middleName") val middleName: String?,
    @SerializedName("lastName") val lastName: String?,
    @SerializedName("gender") val gender: Int,
) : Parcelable