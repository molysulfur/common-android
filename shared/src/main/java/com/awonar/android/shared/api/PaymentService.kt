package com.awonar.android.shared.api

import com.awonar.android.model.payment.*
import com.awonar.android.shared.constrant.BuildConfig
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface PaymentService {

    @GET("v1/withdrawal")
    fun getWithdrawalHistory(
        @Query("page") page: Int,
        @Query("limit") limit: Int = 10
    ): Call<WithdrawHistoryResponse>

    @GET("v1/deposit")
    fun getDepositHistory(
        @Query("page") page: Int,
        @Query("limit") limit: Int = 10
    ): Call<DepositHistoryResponse>

    @POST("v1/withdrawal/local-online-banking")
    fun withdrawalBanking(@Body body: WithdrawRequest): Call<Withdraw>

    @POST("v1/otp/withdrawal")
    fun getOTPWithdrawal(@Body body: WithdrawOTPRequest): Call<OTP>

    @POST("v1/deposit/qrcode")
    fun getQrcode(@Body body: DepositRequest): Call<QRCode>

    @GET("v1/payment/setting/{id}")
    fun getCurrenciesForPayment(@Path("id") id: String): Call<PaymentSetting>

    @GET("v1/payment/method")
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