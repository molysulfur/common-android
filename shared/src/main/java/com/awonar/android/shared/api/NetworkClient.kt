package com.awonar.android.shared.api

import com.awonar.android.shared.db.hawk.AccessTokenManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class NetworkClient constructor(
    private val accessTokenManager: AccessTokenManager,
) {

    fun getClient(interceptorList: List<Interceptor>): OkHttpClient {
        return OkHttpClient().newBuilder().apply {
            addInterceptor(getRequestInterceptor())
            addInterceptor(getHttpLoggingInterceptor())
            interceptorList.forEach { interceptor: Interceptor -> addInterceptor(interceptor) }
        }.build()
    }

    private fun getHttpLoggingInterceptor(): Interceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }


    private fun getRequestInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request().newBuilder().apply {
                addHeader("Content-Type", "application/json;charset=utf-8")
                addHeader("Authorization", "Bearer ${accessTokenManager.getAccessToken()}")
                addHeader("Accept", "application/json;")
            }.build()
            chain.proceed(request)
        }
    }

}
