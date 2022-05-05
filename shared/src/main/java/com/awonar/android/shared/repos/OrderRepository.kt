package com.awonar.android.shared.repos

import com.awonar.android.model.order.*
import com.awonar.android.model.portfolio.ExitOrder
import com.awonar.android.model.portfolio.Position
import com.awonar.android.shared.api.OrderService
import com.molysulfur.library.network.DirectNetworkFlow
import retrofit2.Response
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val orderService: OrderService
) {

    fun deleteOrder(id: String) = object : DirectNetworkFlow<String, Any, Any>() {
        override fun createCall(): Response<Any> =
            orderService.delete(id).execute()

        override fun convertToResultType(response: Any): Any =
            response

        override fun onFetchFailed(errorMessage: String) {
            println(errorMessage)
        }

    }.asFlow()


    fun openOrder(request: OpenOrderRequest) =
        object : DirectNetworkFlow<OpenOrderRequest, OpenOrderResponse, OpenOrderResponse>() {
            override fun createCall(): Response<OpenOrderResponse> = if (request.isEntry) {
                orderService.openEntryOrder(request).execute()
            } else {
                orderService.openOrder(request).execute()
            }

            override fun convertToResultType(response: OpenOrderResponse): OpenOrderResponse =
                response

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }

        }.asFlow()

    fun editOrder(request: UpdateOrderRequest) =
        object : DirectNetworkFlow<UpdateOrderRequest, Position, Position>() {
            override fun createCall(): Response<Position> =
                orderService.edit(request.id, request = request).execute()

            override fun convertToResultType(response: Position): Position =
                response

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }

        }.asFlow()

    fun exitOrder(id: String) = object : DirectNetworkFlow<String, ExitOrder, ExitOrder>() {
        override fun createCall(): Response<ExitOrder> =
            orderService.exitOrder(ExitOrderRequest(id)).execute()

        override fun convertToResultType(response: ExitOrder): ExitOrder =
            response

        override fun onFetchFailed(errorMessage: String) {
            println(errorMessage)
        }

    }.asFlow()

    fun exitOrder(request: ExitOrderPartialRequest) =
        object : DirectNetworkFlow<ExitOrderPartialRequest, ExitOrder, ExitOrder>() {
            override fun createCall(): Response<ExitOrder> =
                orderService.exitOrder(request).execute()

            override fun convertToResultType(response: ExitOrder): ExitOrder =
                response

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }

        }.asFlow()
}