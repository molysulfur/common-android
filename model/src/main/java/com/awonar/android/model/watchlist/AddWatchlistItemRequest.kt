package com.awonar.android.model.watchlist

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddWatchlistItemRequest(
    @SerializedName("folder_id") val folderId :String,
    @SerializedName("instrumentId") val instrumentId: Int = 0,
    @SerializedName("uid") val uid: String? = null,
) : Parcelable