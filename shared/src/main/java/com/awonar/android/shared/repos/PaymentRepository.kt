package com.awonar.android.shared.repos

import com.awonar.android.model.payment.*
import com.awonar.android.shared.api.PaymentService
import com.molysulfur.library.network.DirectNetworkFlow
import com.molysulfur.library.result.Result
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PaymentRepository @Inject constructor(
    private val service: PaymentService
) {
    fun getMethods(type: String) =
        object : DirectNetworkFlow<Unit, List<MethodPayment>, List<MethodPayment>>() {
            override fun createCall(): Response<List<MethodPayment>> =
                service.getMethods(type).execute()

            override fun convertToResultType(response: List<MethodPayment>): List<MethodPayment> =
                response

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }
        }.asFlow()

    fun getPaymentSetting(request: String): Flow<Result<PaymentSetting?>> =
        object : DirectNetworkFlow<Unit, PaymentSetting, PaymentSetting>() {
            override fun createCall(): Response<PaymentSetting> =
                service.getCurrenciesForPayment(request).execute()

            override fun convertToResultType(response: PaymentSetting): PaymentSetting =
                response

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }
        }.asFlow()

    fun getDepositQrcode(request: DepositRequest): Flow<Result<QRCode?>> =
        object : DirectNetworkFlow<DepositRequest, QRCode, QRCode>() {
            override fun createCall(): Response<QRCode> =
                service.getQrcode(request).execute()

            override fun convertToResultType(response: QRCode): QRCode =
                response

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }
        }.asFlow()

    fun withdrawOTP(request: WithdrawOTPRequest): Flow<Result<OTP?>> =
        object : DirectNetworkFlow<Unit, OTP, OTP>() {
            override fun createCall(): Response<OTP> =
                service.getOTPWithdrawal(request).execute()

            override fun convertToResultType(response: OTP): OTP =
                response

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }
        }.asFlow()

    fun requestWithdraw(request: WithdrawRequest): Flow<Result<Withdraw?>> =
        object : DirectNetworkFlow<WithdrawRequest, Withdraw, Withdraw>() {
            override fun createCall(): Response<Withdraw> =
                service.withdrawalBanking(request).execute()

            override fun convertToResultType(response: Withdraw): Withdraw =
                response

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }
        }.asFlow()
}