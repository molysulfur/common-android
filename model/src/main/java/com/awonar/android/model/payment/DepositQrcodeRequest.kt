package com.awonar.android.model.payment

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DepositQrcodeRequest(
    @SerializedName("cardId") val cardId: String,
    @SerializedName("dollarAmount") val amountUsd: Float,
    @SerializedName("localAmount") val amount: Float,
    @SerializedName("localCurrencyId") val cureencyId: String,
    @SerializedName("paymentMethodId") val methodId: String,
    @SerializedName("redirectUrl") val redirect: String,
) : Parcelable
