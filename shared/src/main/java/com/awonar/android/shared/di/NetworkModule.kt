package com.awonar.android.shared.di

import android.app.Application
import com.awonar.android.shared.api.AuthService
import com.awonar.android.shared.api.NetworkClient
import com.awonar.android.shared.api.UserService
import com.awonar.android.shared.db.hawk.AccessTokenManager
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
    fun provideAuthService(client: NetworkClient): AuthService {
        return AuthService.create(client)
    }

    @Singleton
    @Provides
    fun provideUserService(client: NetworkClient): UserService {
        return UserService.create(client)
    }
}