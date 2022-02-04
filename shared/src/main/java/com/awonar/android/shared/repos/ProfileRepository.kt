package com.awonar.android.shared.repos

import com.awonar.android.model.portfolio.PendingOrder
import com.awonar.android.model.user.DrawdownResponse
import com.awonar.android.model.user.StatGainResponse
import com.awonar.android.model.user.StatRiskResponse
import com.awonar.android.shared.api.ProfileService
import com.molysulfur.library.network.DirectNetworkFlow
import com.molysulfur.library.result.Result
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
    private val service: ProfileService,
) {

    fun getRiskStatistic(uid: String) =
        object : DirectNetworkFlow<Unit, StatRiskResponse, StatRiskResponse>() {
            override fun createCall(): Response<StatRiskResponse> =
                service.getRiskStatistic(uid).execute()

            override fun convertToResultType(response: StatRiskResponse): StatRiskResponse =
                response

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }
        }.asFlow()

    fun getGrowthStatistic(uid: String) =
        object : DirectNetworkFlow<Unit, StatGainResponse, StatGainResponse>() {
            override fun createCall(): Response<StatGainResponse> =
                service.getGrowthStatistic(uid).execute()

            override fun convertToResultType(response: StatGainResponse): StatGainResponse =
                response

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }
        }.asFlow()

    fun getDrawdown(uid: String): Flow<Result<DrawdownResponse?>> =
        object : DirectNetworkFlow<Unit, DrawdownResponse, DrawdownResponse>() {
            override fun createCall(): Response<DrawdownResponse> =
                service.getDrawdown(uid).execute()

            override fun convertToResultType(response: DrawdownResponse): DrawdownResponse =
                response

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }
        }.asFlow()
}