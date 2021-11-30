package com.awonar.android.shared.repos

import com.awonar.android.model.order.OpenOrderRequest
import com.awonar.android.model.order.OpenOrderResponse
import com.awonar.android.model.order.UpdateOrderRequest
import com.awonar.android.shared.api.OrderService
import com.molysulfur.library.network.DirectNetworkFlow
import retrofit2.Response
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val orderService: OrderService
) {


    fun openOrder(request: OpenOrderRequest) =
        object : DirectNetworkFlow<OpenOrderRequest, OpenOrderResponse, OpenOrderResponse>() {
            override fun createCall(): Response<OpenOrderResponse> =
                orderService.openOrder(request).execute()

            override fun convertToResultType(response: OpenOrderResponse): OpenOrderResponse =
                response

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }

        }.asFlow()

    fun editOrder(request: UpdateOrderRequest) =
        object : DirectNetworkFlow<UpdateOrderRequest, Any, Any>() {
            override fun createCall(): Response<Any> =
                orderService.edit(request.id, request = request).execute()

            override fun convertToResultType(response: Any): Any =
                response

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }

        }.asFlow()
}