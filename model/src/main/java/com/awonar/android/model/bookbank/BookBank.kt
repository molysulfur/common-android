package com.awonar.android.model.bookbank

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookBank(
    @SerializedName("id") val id: String?,
    @SerializedName("bank") val bank: String?,
    @SerializedName("name") val accountName: String?,
    @SerializedName("number") val accountNumber: String?,
    @SerializedName("bankName") val bankName: String?,
    @SerializedName("countryId") val countryId: String?,
    @SerializedName("type") val type: String?
) : Parcelable
