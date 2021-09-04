package com.awonar.android.model.bookbank

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookBankRequest(
    @SerializedName("bank") val bank: String?,
    @SerializedName("bookBank") val bookBankImage: List<String?>,
    @SerializedName("countryId") val countryId: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("number") val number: String?,
    @SerializedName("type") val type: String?,
) : Parcelable