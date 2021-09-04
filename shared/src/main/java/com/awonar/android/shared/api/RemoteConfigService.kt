package com.awonar.android.shared.api

import com.awonar.android.model.settting.Bank
import com.awonar.android.model.settting.Country
import com.awonar.android.shared.constrant.BuildConfig
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface RemoteConfigService {

    @GET("api/v1/countries")
    fun countries(): Call<List<Country>>

    @GET("api/v1/bank")
    fun banks(): Call<List<Bank>>


    companion object {

        fun create(client: NetworkClient): RemoteConfigService = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client.getClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RemoteConfigService::class.java)

    }
}