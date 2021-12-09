package com.awonar.android.model.payment

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MethodPayment(
    @SerializedName("depositProcessingTime") val depositProcessingTime: String?,
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("paymentChannel") val paymentChannel: String?,
    @SerializedName("picture") val picture: String?,
    @SerializedName("withdrawProcessingTime") val withdrawProcessingTime: String?,
) : Parcelable
