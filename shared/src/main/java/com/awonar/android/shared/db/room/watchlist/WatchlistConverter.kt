package com.awonar.android.shared.db.room.watchlist

import androidx.room.TypeConverter
import com.awonar.android.model.watchlist.WatchlistInfo
import com.awonar.android.shared.extension.genericType
import com.google.gson.Gson

class WatchlistConverter {

    @TypeConverter
    fun fromListString(list: List<WatchlistInfo>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun toListString(stringList: String): List<WatchlistInfo> {
        return Gson().fromJson(stringList, genericType<List<WatchlistInfo>>())
    }
}