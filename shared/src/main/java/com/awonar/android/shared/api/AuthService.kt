package com.awonar.android.shared.api

import com.awonar.android.model.Auth
import com.awonar.android.model.SignInGoogleRequest
import com.awonar.android.model.SignInRequest
import com.awonar.android.model.SignOutResponse
import com.awonar.android.shared.constrant.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthService {

    @POST("api/v1/users/login/facebook")
    fun signInWithFacebook(@Body signInRequest: SignInGoogleRequest): Call<Auth?>

    @POST("api/v1/users/login/google")
    fun signInWithGoogle(@Body signInRequest: SignInGoogleRequest): Call<Auth?>

    @POST("api/v1/users/login/password")
    fun signInWithPassword(@Body signInRequest: SignInRequest): Call<Auth?>

    @POST("api/v1/users/token")
    fun getRefreshToken(@Body auth: Auth): Call<Auth?>

    @POST("api/v1/users/logout")
    fun signOut(): Call<SignOutResponse?>

    companion object {

        fun create(client: NetworkClient): AuthService = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client.getClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthService::class.java)

    }
}
