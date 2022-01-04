package com.awonar.android.shared.repos

import com.awonar.android.model.socialtrade.CopierRecommended
import com.awonar.android.model.socialtrade.CopierRecommendedResponse
import com.awonar.android.shared.api.SocialTradeService
import com.molysulfur.library.network.DirectNetworkFlow
import com.molysulfur.library.result.Result
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocialTradeRepository @Inject constructor(
    private val service: SocialTradeService
) {


    fun getRecommended(): Flow<Result<List<CopierRecommended>?>> =
        object : DirectNetworkFlow<Unit, List<CopierRecommended>, CopierRecommendedResponse>() {
            override fun createCall(): Response<CopierRecommendedResponse> =
                service.getRecommended().execute()

            override fun convertToResultType(response: CopierRecommendedResponse): List<CopierRecommended> =
                response.traders

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }

        }.asFlow()
}