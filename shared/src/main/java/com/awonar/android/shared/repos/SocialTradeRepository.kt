package com.awonar.android.shared.repos

import com.awonar.android.model.copier.UpdateFundRequest
import com.awonar.android.model.copier.CopiesRequest
import com.awonar.android.model.copier.PauseCopyRequest
import com.awonar.android.model.copier.UpdateCopy
import com.awonar.android.model.portfolio.Copier
import com.awonar.android.model.socialtrade.Trader
import com.awonar.android.model.socialtrade.TradersRequest
import com.awonar.android.model.socialtrade.TradersResponse
import com.awonar.android.shared.api.SocialTradeService
import com.molysulfur.library.network.DirectNetworkFlow
import com.molysulfur.library.result.Result
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class SocialTradeRepository @Inject constructor(
    private val service: SocialTradeService,
) {

    fun getTraders(request: TradersRequest): Flow<Result<List<Trader>?>> =
        object : DirectNetworkFlow<Unit, List<Trader>, TradersResponse>() {
            override fun createCall(): Response<TradersResponse> =
                service.getTraders(sort = request.filter,
                    maxRisk = request.maxRisk,
                    page = request.page,
                    uid = request.uid)
                    .execute()

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

    fun addFund(parameters: UpdateFundRequest) =
        object : DirectNetworkFlow<Unit, Copier?, Copier?>() {
            override fun createCall(): Response<Copier?> =
                service.addFund(parameters).execute()

            override fun convertToResultType(response: Copier?): Copier? =
                response

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }

        }.asFlow()

    fun createCopy(parameters: CopiesRequest) =
        object : DirectNetworkFlow<Unit, Copier?, Copier?>() {
            override fun createCall(): Response<Copier?> =
                service.createCopy(parameters).execute()

            override fun convertToResultType(response: Copier?): Copier? =
                response

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }

        }.asFlow()

    fun stopCopy(parameters: String): Flow<Result<Copier?>> =
        object : DirectNetworkFlow<Unit, Copier?, Copier?>() {
            override fun createCall(): Response<Copier?> =
                service.stopCopy(parameters).execute()

            override fun convertToResultType(response: Copier?): Copier? = response

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }

        }.asFlow()

    fun pauseCopy(parameters: PauseCopyRequest) =
        object : DirectNetworkFlow<PauseCopyRequest, Copier?, Copier?>() {
            override fun createCall(): Response<Copier?> =
                service.updatePauseCopy(parameters).execute()

            override fun convertToResultType(response: Copier?): Copier? = response

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }

        }.asFlow()

    fun updateCopy(parameters: UpdateCopy): Flow<Result<Copier?>> =
        object : DirectNetworkFlow<Unit, Copier?, Copier?>() {
            override fun createCall(): Response<Copier?> =
                service.updateCopy(parameters).execute()

            override fun convertToResultType(response: Copier?): Copier? =
                response

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }

        }.asFlow()
}