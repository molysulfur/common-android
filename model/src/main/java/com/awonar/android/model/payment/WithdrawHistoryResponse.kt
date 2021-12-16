package com.awonar.android.model.payment

import android.os.Parcelable
import com.awonar.android.model.history.HistoryMeta
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class WithdrawHistoryResponse(
    @SerializedName("histories") val histories: List<Withdraw>,
    @SerializedName("meta") val meta: HistoryMeta
) : Parcelable