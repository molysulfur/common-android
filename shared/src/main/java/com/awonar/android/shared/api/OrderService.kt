package com.awonar.android.shared.api

import com.awonar.android.model.order.OpenOrderRequest
import com.awonar.android.model.order.OpenOrderResponse
import com.awonar.android.model.order.UpdateOrderRequest
import com.awonar.android.shared.constrant.BuildConfig
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface OrderService {

    @POST("api/v1/orders")
    fun openOrder(@Body request: OpenOrderRequest): Call<OpenOrderResponse>

    @PUT("api/v1/positions/{id}")
    fun edit(@Path("id") id: String, @Body request: UpdateOrderRequest): Call<Any>

    companion object {

        fun create(client: NetworkClient): OrderService = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client.getClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OrderService::class.java)

    }
}