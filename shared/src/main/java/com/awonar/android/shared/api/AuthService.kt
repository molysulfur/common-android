package com.awonar.android.shared.api

import com.awonar.android.model.Auth
import com.awonar.android.model.SignInRequest
import com.awonar.android.shared.constrant.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("api/v1/users/login/password")
    fun signInWithPassword(@Body signInRequest: SignInRequest): Call<Auth?>

    companion object {
        private const val BASE_URL = BuildConfig.BASE_URL

        fun create(): AuthService {
            val logger =
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .addInterceptor(getRequestInterceptor())
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AuthService::class.java)
        }

        private fun getRequestInterceptor(): Interceptor {
            return Interceptor { chain ->
                val request = chain.request().newBuilder().apply {
                    addHeader("Content-Type", "application/json;charset=utf-8")
                    addHeader("Accept", "application/json;")
                }.build()
                chain.proceed(request)
            }
        }


    }
}
