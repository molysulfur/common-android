package com.awonar.android.model.watchlist

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.awonar.android.model.market.Instrument
import com.awonar.android.model.socialtrade.Trader
import kotlinx.parcelize.Parcelize


@Entity(tableName = "watchlist_folders")
@Parcelize
data class Folder(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "name") var name: String?,
    @ColumnInfo(name = "totalItem") var totalItem: Int,
    @ColumnInfo(name = "static") var static: Boolean,
    @ColumnInfo(name = "recentlyInvested") var recentlyInvested: Boolean,
    @ColumnInfo(name = "default") var default: Boolean,
    @ColumnInfo(name = "infos") var infos: List<WatchlistInfo>,
) : Parcelable


@Parcelize
data class WatchlistInfo(
    val id: String?,
    val instrumentId: Int,
    val uid: String?,
    val image: String?,
    val title: String?,
    val subTitle: String?,
    val risk: Int = 0,
    val gain: Float = 0f,
    val type: String?,
) : Parcelable