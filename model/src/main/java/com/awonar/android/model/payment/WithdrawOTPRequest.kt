package com.awonar.android.model.payment

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class WithdrawOTPRequest(
    @SerializedName("dollarAmount") val amount: Float
) : Parcelable {
}