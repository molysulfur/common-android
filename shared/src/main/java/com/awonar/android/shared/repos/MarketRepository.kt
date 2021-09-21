package com.awonar.android.shared.repos

import com.awonar.android.model.market.Instrument
import com.awonar.android.model.market.InstrumentProfile
import com.awonar.android.model.market.InstrumentResponse
import com.awonar.android.shared.api.InstrumentService
import com.molysulfur.library.network.DirectNetworkFlow
import com.molysulfur.library.network.NetworkFlow
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class MarketRepository @Inject constructor(
    private val instrumentService: InstrumentService
) {

    fun getInstrument(id: Int) =
        object : DirectNetworkFlow<Int, InstrumentProfile?, InstrumentProfile?>() {
            override fun createCall(): Response<InstrumentProfile?> {
                return instrumentService.getInstrument(id).execute()
            }

            override fun convertToResultType(response: InstrumentProfile?): InstrumentProfile? =
                response

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }

        }.asFlow()

    fun getInstrumentList() =
        object : DirectNetworkFlow<Boolean, List<Instrument>, InstrumentResponse>() {

            override fun createCall(): Response<InstrumentResponse> {
                return instrumentService.getInstruments().execute()
            }

            override fun convertToResultType(response: InstrumentResponse): List<Instrument> {
                return response.instruments
            }

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }

        }.asFlow()
}