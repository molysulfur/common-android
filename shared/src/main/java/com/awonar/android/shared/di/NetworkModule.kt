package com.awonar.android.shared.di

import android.app.Application
import com.awonar.android.shared.api.*
import com.awonar.android.shared.db.hawk.AccessTokenManager
import com.awonar.android.shared.steaming.QuoteSteamingManager
import com.awonar.android.shared.utils.HawkUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideAccessTokenManager(app: Application) = AccessTokenManager(hawk = HawkUtil(app))

    @Singleton
    @Provides
    fun provideNetworkClient(accessTokenManager: AccessTokenManager) =
        NetworkClient(accessTokenManager)

    @Singleton
    @Provides
    fun provideInstrumentService(client: NetworkClient): InstrumentService {
        return InstrumentService.create(client)
    }

    @Singleton
    @Provides
    fun provideQuoteSteaming(client: NetworkClient): QuoteSteamingManager {
        return QuoteSteamingManager(client)
    }

    @Singleton
    @Provides
    fun provideAuthService(client: NetworkClient): AuthService {
        return AuthService.create(client)
    }

    @Singleton
    @Provides
    fun provideUserService(client: NetworkClient): UserService {
        return UserService.create(client)
    }

    @Singleton
    @Provides
    fun provideRemoteConfigService(client: NetworkClient): RemoteConfigService {
        return RemoteConfigService.create(client)
    }

    @Singleton
    @Provides
    fun providePortFolioService(client: NetworkClient): PortfolioService {
        return PortfolioService.create(client)
    }

    @Singleton
    @Provides
    fun provideCurrenciesService(client: NetworkClient): CurrenciesService {
        return CurrenciesService.create(client)
    }
}