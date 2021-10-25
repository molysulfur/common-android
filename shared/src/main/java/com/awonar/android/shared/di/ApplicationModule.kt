package com.awonar.android.shared.di

import android.app.Application
import com.awonar.android.shared.utils.HawkUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideHawk(app: Application) = HawkUtil(app.applicationContext)


}