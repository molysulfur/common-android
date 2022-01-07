package com.awonar.android.shared.repos

import com.awonar.android.model.socialtrade.Trader
import com.awonar.android.model.socialtrade.TradersRequest
import com.awonar.android.model.socialtrade.TradersResponse
import com.awonar.android.shared.api.SocialTradeService
import com.molysulfur.library.network.DirectNetworkFlow
import com.molysulfur.library.result.Result
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

class SocialTradeRepository @Inject constructor(
    private val service: SocialTradeService
) {

    fun getTraders(request: TradersRequest): Flow<Result<List<Trader>?>> =
        object : DirectNetworkFlow<Unit, List<Trader>, TradersResponse>() {
            override fun createCall(): Response<TradersResponse> =
                service.getTraders(sort = request.filter, page = request.page, uid = request.uid).execute()

            override fun convertToResultType(response: TradersResponse): List<Trader> =
                response.traders

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }

        }.asFlow()


    fun getRecommended(): Flow<Result<List<Trader>?>> =
        object : DirectNetworkFlow<Unit, List<Trader>, TradersResponse>() {
            override fun createCall(): Response<TradersResponse> =
                service.getRecommended().execute()

            override fun convertToResultType(response: TradersResponse): List<Trader> =
                response.traders

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }

        }.asFlow()
}