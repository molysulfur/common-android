package com.awonar.android.shared.api

import com.awonar.android.model.order.OpenOrderRequest
import com.awonar.android.model.order.OpenOrderResponse
import com.awonar.android.shared.constrant.BuildConfig
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface OrderService {

    @POST("api/v1/orders")
    fun openOrder(@Body request : OpenOrderRequest) : Call<OpenOrderResponse>

    companion object {

        fun create(client: NetworkClient): OrderService = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client.getClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OrderService::class.java)

    }
}