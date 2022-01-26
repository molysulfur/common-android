package com.awonar.android.shared.db.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.awonar.android.model.Conversion
import com.awonar.android.model.market.Instrument
import com.awonar.android.model.tradingdata.TradingData
import com.awonar.android.model.watchlist.Folder
import com.awonar.android.shared.db.room.conversionrate.ConversionRateDao
import com.awonar.android.shared.db.room.instrument.InstrumentDao
import com.awonar.android.shared.db.room.trading.TradingDataDao
import com.awonar.android.shared.db.room.watchlist.WatchlistConverter
import com.awonar.android.shared.db.room.watchlist.WatchlistFolderDao

@Database(
    entities = [TradingData::class, Conversion::class, Instrument::class, Folder::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    DatabaseConverter::class,
    WatchlistConverter::class
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tradingDataDao(): TradingDataDao
    abstract fun conversionDao(): ConversionRateDao
    abstract fun watchlistFolderDao(): WatchlistFolderDao
    abstract fun instrumentDao(): InstrumentDao

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