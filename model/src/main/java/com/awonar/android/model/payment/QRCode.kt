package com.awonar.android.model.payment

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class QRCode(
    @SerializedName("dollarAmount") val amountUsd: Float,
    @SerializedName("localAmount") val localAmount: Float,
    @SerializedName("localRate") val rate: Float,
    @SerializedName("localCurrencyId") val currencyId: String,
    @SerializedName("qrCode") val qrCode: String,
    @SerializedName("referenceNo") val referenceNo: String,
) : Parcelable