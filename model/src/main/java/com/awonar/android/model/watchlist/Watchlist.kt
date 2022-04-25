package com.awonar.android.model.watchlist

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "watchlist_folders")
@Parcelize
data class Folder constructor(
    @PrimaryKey
    @ColumnInfo(name = "id") var id: String,
    @ColumnInfo(name = "name") var name: String?,
    @ColumnInfo(name = "totalItem") var totalItem: Int,
    @ColumnInfo(name = "static") var static: Boolean,
    @ColumnInfo(name = "recentlyInvested") var recentlyInvested: Boolean,
    @ColumnInfo(name = "default") var default: Boolean,
    @ColumnInfo(name = "infos") var infos: List<WatchlistInfo>,
) : Parcelable {

    constructor() : this("", "", 0, false, false, false, emptyList())
}


@Parcelize
data class WatchlistInfo constructor(
    var id: String?,
    var instrumentId: Int,
    var uid: String?,
    var image: String?,
    var title: String?,
    var subTitle: String?,
    var risk: Int = 0,
    var gain: Float = 0f,
    var type: String?,
) : Parcelable