package com.awonar.android.model.copier

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddFundRequest(
    @SerializedName("amount") val amount: Float,
    @SerializedName("id") val id: String
) : Parcelable