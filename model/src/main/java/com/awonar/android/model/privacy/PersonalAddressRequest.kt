package com.awonar.android.model.privacy

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PersonalAddressRequest(
    @SerializedName("address") val address: String?,
    @SerializedName("city") val city: String?,
    @SerializedName("country") val country: String?,
    @SerializedName("postalCode") val postalCode: String?,
) : Parcelable