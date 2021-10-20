package com.awonar.android.shared.repos

import com.awonar.android.model.order.OpenOrderRequest
import com.awonar.android.model.order.OpenOrderResponse
import com.awonar.android.shared.api.InstrumentService
import com.awonar.android.shared.api.OrderService
import com.awonar.android.shared.db.room.trading.TradingDataDao
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
}