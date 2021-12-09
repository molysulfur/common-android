package com.awonar.android.shared.api

import com.awonar.android.model.payment.MethodPayment
import com.awonar.android.shared.constrant.BuildConfig
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface PaymentService {

    @GET("api/v1/payment/method")
    fun getMethods(@Query("type") type: String): Call<List<MethodPayment>>


    companion object {

        fun create(client: NetworkClient): PaymentService = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client.getClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PaymentService::class.java)

    }
}