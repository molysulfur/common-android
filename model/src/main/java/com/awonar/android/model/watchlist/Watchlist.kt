package com.awonar.android.model.watchlist

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "watchlist_folders")
@Parcelize
class Folder(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "totalItem") val totalItem: Int,
    @ColumnInfo(name = "static") val static: Boolean,
    @ColumnInfo(name = "recentlyInvested") val recentlyInvested: Boolean,
    @ColumnInfo(name = "default") val default: Boolean,
    @ColumnInfo(name = "images") val images: List<String>,
) : Parcelable