package com.awonar.android.shared.api

import com.awonar.android.model.market.Instrument
import com.awonar.android.model.market.InstrumentResponse
import com.awonar.android.shared.constrant.BuildConfig
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface InstrumentService {

    @GET("api/v1/instruments/slim/filter")
    fun getInstruments(): Call<InstrumentResponse>


    companion object {

        fun create(client: NetworkClient): InstrumentService = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client.getClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(InstrumentService::class.java)

    }
}