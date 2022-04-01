package com.awonar.android.model.user

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class StatRiskResponse(
    @SerializedName("risks") val risks: List<StatRisk>,
) : Parcelable