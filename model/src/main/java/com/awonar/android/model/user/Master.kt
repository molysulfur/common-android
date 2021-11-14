package com.awonar.android.model.user

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Master(
    @SerializedName("id") val id: String,
    @SerializedName("displayFullName") val displayFullName: Boolean,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("middleName") val middleName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("picture") val picture: String,
    @SerializedName("private") val private: Boolean,
    @SerializedName("username") val username: Boolean,
) : Parcelable