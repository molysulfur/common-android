package com.awonar.android.shared.repos

import com.awonar.android.model.portfolio.PendingOrder
import com.awonar.android.model.user.StatGainResponse
import com.awonar.android.shared.api.ProfileService
import com.molysulfur.library.network.DirectNetworkFlow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
    private val service: ProfileService,
) {

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
}