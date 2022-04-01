package com.awonar.android.shared.repos

import com.awonar.android.model.marketprofile.FinancialResponse
import com.awonar.android.model.marketprofile.MarketOverview
import com.awonar.android.shared.api.MarketProfileService
import com.molysulfur.library.network.DirectNetworkFlow
import retrofit2.Response
import javax.inject.Inject

class MarketProfileRepository @Inject constructor(
    private val marketProfileService: MarketProfileService,
) {

    fun getFinancialInfo(instrumentId: Int) =
        object : DirectNetworkFlow<Int, FinancialResponse, FinancialResponse>() {
            override fun createCall(): Response<FinancialResponse> =
                marketProfileService.getFinancial(instrumentId).execute()

            override fun convertToResultType(response: FinancialResponse): FinancialResponse =
                response

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }
        }.asFlow()

    fun getOverView(instrumentId: Int) =
        object : DirectNetworkFlow<Int, MarketOverview, MarketOverview>() {
            override fun createCall(): Response<MarketOverview> =
                marketProfileService.getOverView(instrumentId).execute()

            override fun convertToResultType(response: MarketOverview): MarketOverview = response
            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }
        }.asFlow()
}