package com.awonar.android.model.payment

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class OTP(
    @SerializedName("createdAt") val createdAt: String? = null,
    @SerializedName("destination") val destination: String? = null,
    @SerializedName("expirationAt") val expDate: String? = null,
    @SerializedName("expirationTimestamp") val exp: Long = 0,
    @SerializedName("id", alternate = ["otp"]) val id: String? = null,
    @SerializedName("referenceNo1") val referenceNo1: String? = null,
) : Parcelable