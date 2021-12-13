package com.awonar.android.shared.api

import com.awonar.android.model.payment.DepositQrcodeRequest
import com.awonar.android.model.payment.MethodPayment
import com.awonar.android.model.payment.PaymentSetting
import com.awonar.android.model.payment.QRCode
import com.awonar.android.shared.constrant.BuildConfig
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface PaymentService {

    @POST("api/v1/deposit/qrcode")
    fun getQrcode(@Body body: DepositQrcodeRequest): Call<QRCode>

    @GET("api/v1/payment/setting/{id}")
    fun getCurrenciesForPayment(@Path("id") id: String): Call<PaymentSetting>

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