package com.awonar.android.shared.api

import com.awonar.android.model.order.*
import com.awonar.android.model.portfolio.ExitOrder
import com.awonar.android.model.portfolio.Position
import com.awonar.android.shared.constrant.BuildConfig
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface OrderService {

    @POST("v1/exit-orders")
    fun exitOrder(@Body request: ExitOrderRequest): Call<ExitOrder>

    @POST("v1/exit-orders")
    fun exitOrder(@Body request: ExitOrderPartialRequest): Call<ExitOrder>

    @POST("v1/orders")
    fun openOrder(@Body request: OpenOrderRequest): Call<OpenOrderResponse>

    @POST("v1/entry-orders")
    fun openEntryOrder(@Body request: OpenOrderRequest): Call<OpenOrderResponse>

    @PUT("v1/positions/{id}")
    fun edit(@Path("id") id: String, @Body request: UpdateOrderRequest): Call<Position>

    @DELETE("v1/positions/{id}")
    fun delete(@Path("id") id: String): Call<Any>

    companion object {

        fun create(client: NetworkClient): OrderService = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client.getClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OrderService::class.java)

    }
}