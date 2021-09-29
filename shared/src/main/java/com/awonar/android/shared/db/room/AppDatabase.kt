package com.awonar.android.shared.db.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [TradingData::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    DatabaseConverter::class
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tradingDataDao(): TradingDataDao

    companion object {

        private var INSTANCE: AppDatabase? = null
        private const val DATABASE_NAME = "awonar_db"

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room
                    .databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        DATABASE_NAME
                    ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }


    }


}