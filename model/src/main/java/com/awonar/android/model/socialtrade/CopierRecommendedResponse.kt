package com.awonar.android.model.socialtrade

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CopierRecommendedResponse(
    @SerializedName("traders") val traders : List<CopierRecommended>
) : Parcelable {
}