package com.awonar.android.model.payment

import android.os.Parcelable
import com.awonar.android.model.bookbank.BookBank
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Deposit(
    @SerializedName("id") val id: String?,
    @SerializedName("depositNo") val depositNo: Int,
    @SerializedName("comment") val comment: String?,
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("dollarAmount") val dollarAmount: Float,
    @SerializedName("fee") val fee: Float,
    @SerializedName("paymentMethod") val paymentMethod: MethodPayment?,
    @SerializedName("status") val status: String?,
    @SerializedName("verifyBankAccount") val bookBank: BookBank?,
) : Parcelable