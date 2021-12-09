package com.awonar.android.shared.repos

import com.awonar.android.model.payment.MethodPayment
import com.awonar.android.shared.api.PaymentService
import com.molysulfur.library.network.DirectNetworkFlow
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
}