package com.awonar.android.model.privacy

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class PersonalCardIdRequest(
    @SerializedName("identityDocuments") var idDocs: List<String?> = listOf(),
    @SerializedName("identityNumber") var idNo: String? = null,
    @SerializedName("identityType") var idType: Int = -1,
) : Parcelable