package com.awonar.android.model.payment

import com.google.gson.annotations.SerializedName

data class WithdrawRequest(
    @SerializedName("dollarAmount") val amount: Float,
    @SerializedName("note") val note: String?,
    @SerializedName("verify") val verify: WithdrawVerify?,
    @SerializedName("verifyBankAccountId") val verifyBankAccountId: String?,
)

data class WithdrawVerify(
    @SerializedName("otp") val otp: Int,
    @SerializedName("referenceNo1") val ref: String?,
)

