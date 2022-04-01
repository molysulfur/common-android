package com.awonar.android.model.watchlist

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddWatchlistRequest(
    @SerializedName("name") val name: String,
) : Parcelable