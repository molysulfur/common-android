package com.awonar.android.model.payment

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentSetting(
    @SerializedName("allowDepositCurrencies") val allowDepositCurrencies: List<String?>,
    @SerializedName("allowWithdrawCurrencies") val allowWithdrawCurrencies: List<String?>,
    @SerializedName("commissionDepositDollar") val commissionDepositDollar: Float,
    @SerializedName("commissionWithdrawDollar") val commissionWithdrawDollar: Float,
    @SerializedName("maxDepositDollar") val maxDepositDollar: Float,
    @SerializedName("maxWithdrawDollar") val maxWithdrawDollar: Float,
    @SerializedName("minDepositDollar") val minDepositDollar: Float,
    @SerializedName("minWithdrawDollar") val minWithdrawDollar: Float,
    @SerializedName("rateFeeDepositPercentage") val rateFeeDepositPercentage: Float,
    @SerializedName("rateFeeWithdrawPercentage") val rateFeeWithdrawPercentage: Float,
) : Parcelable