package com.awonar.android.model.user

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PersonalInfoResponse(
    @SerializedName("address") val address: String?,
    @SerializedName("birthDate") val birthDate: String?,
    @SerializedName("city") val city: String?,
    @SerializedName("country") val country: String?,
    @SerializedName("draft") val draft: Boolean,
    @SerializedName("finish1") val finish1: Boolean,
    @SerializedName("finish2") val finish2: Boolean,
    @SerializedName("finish3") val finish3: Boolean,
    @SerializedName("firstName") val firstName: String?,
    @SerializedName("middleName") val middleName: String?,
    @SerializedName("lastName") val lastName: String?,
    @SerializedName("phoneNumber") val phoneNumber: String?,
    @SerializedName("postalCode") val postalCode: String?,
    @SerializedName("identityNumber") val identityNumber: String?,
    @SerializedName("gender") val gender: Int,
) : Parcelable
