package com.awonar.android.model.watchlist

import android.os.Parcelable
import com.awonar.android.model.socialtrade.Trader
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class WatchlistFolder(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("totalItem") val totalItem: Int,
    @SerializedName("static") val static: Boolean,
    @SerializedName("recentlyInvested") val recentlyInvested: Boolean,
    @SerializedName("default") val default: Boolean,
    @SerializedName("items") val items: List<WatchlistFolderItem>?,
) : Parcelable

@Parcelize
data class WatchlistFolderItem(
    @SerializedName("id") val id: String?,
    @SerializedName("uid") val uid: String?,
    @SerializedName("instrumentId") val instrumentId: Int?,
    @SerializedName("type") val type: String?,
    @SerializedName("trader") val trader: Trader?,
) : Parcelable
