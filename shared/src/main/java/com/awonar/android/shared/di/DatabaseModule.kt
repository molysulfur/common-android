package com.awonar.android.shared.di

import android.app.Application
import android.content.Context
import com.awonar.android.shared.db.room.AppDatabase
import com.awonar.android.shared.db.room.TradingDataDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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


}