package com.awonar.android.shared.db.room

import androidx.room.TypeConverter
import com.awonar.android.shared.extension.genericType
import com.google.gson.Gson

class DatabaseConverter {
    @TypeConverter
    fun fromListString(list: List<String>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun toListString(stringList: String): List<String> {
        return Gson().fromJson(stringList, genericType<List<String>>())
    }

}

