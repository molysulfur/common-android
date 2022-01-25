package com.awonar.android.shared.di

import android.app.Application
import com.awonar.android.shared.db.room.AppDatabase
import com.awonar.android.shared.db.room.conversionrate.ConversionRateDao
import com.awonar.android.shared.db.room.instrument.InstrumentDao
import com.awonar.android.shared.db.room.trading.TradingDataDao
import com.awonar.android.shared.db.room.watchlist.WatchlistFolderDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(app: Application): AppDatabase {
        return AppDatabase.getInstance(app.applicationContext)
    }

    @Provides
    fun provideTradingDataDao(appDatabase: AppDatabase): TradingDataDao {
        return appDatabase.tradingDataDao()
    }

    @Provides
    fun provideConversionDao(appDatabase: AppDatabase): ConversionRateDao {
        return appDatabase.conversionDao()
    }

    @Provides
    fun provideWatchlistFolderDao(appDatabase: AppDatabase): WatchlistFolderDao {
        return appDatabase.watchlistFolderDao()
    }

    @Provides
    fun provideInstrumentDao(appDatabase: AppDatabase): InstrumentDao {
        return appDatabase.instrumentDao()
    }

}