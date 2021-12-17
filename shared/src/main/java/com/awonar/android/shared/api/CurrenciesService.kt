package com.awonar.android.shared.api

import com.awonar.android.model.Conversion
import com.awonar.android.model.currency.Currency
import com.awonar.android.shared.constrant.BuildConfig
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface CurrenciesService {

    @GET("/api/v1/currencies/{currency}")
    fun getCurrencyRate(@Path("currency") currency: String): Call<Currency>

    @GET("/api/v1/conversions/usd-rate")
    fun getConversionRate(): Call<List<Conversion>>

    companion object {

        fun create(client: NetworkClient): CurrenciesService = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client.getClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrenciesService::class.java)

    }
}