package com.awonar.android.shared.repos

import com.awonar.android.model.market.Instrument
import com.awonar.android.model.market.InstrumentProfile
import com.awonar.android.model.market.InstrumentResponse
import com.awonar.android.model.market.Quote
import com.awonar.android.shared.api.InstrumentService
import com.awonar.android.model.tradingdata.TradingData
import com.awonar.android.shared.db.room.trading.TradingDataDao
import com.molysulfur.library.network.DirectNetworkFlow
import com.molysulfur.library.network.NetworkFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class MarketRepository @Inject constructor(
    private val instrumentService: InstrumentService,
    private val tradingDataDao: TradingDataDao
) {

    fun getLastPriceWithId(id: Int) = object : DirectNetworkFlow<Int, Quote, List<Quote>>() {
        override fun createCall(): Response<List<Quote>> =
            instrumentService.getLastQuote(listOf(id)).execute()

        override fun convertToResultType(response: List<Quote>): Quote = response[0]
        override fun onFetchFailed(errorMessage: String) {
            println(errorMessage)
        }
    }.asFlow()

    fun getTradingDataById(id: Int): TradingData {
        return tradingDataDao.loadById(id)
    }

    fun getTradingData(needFresh: Boolean) =
        object : NetworkFlow<Boolean, List<TradingData>, List<TradingData>>() {
            override fun shouldFresh(data: List<TradingData>?): Boolean = needFresh || data == null

            override fun createCall(): Response<List<TradingData>> =
                instrumentService.getTradingData().execute()

            override fun convertToResultType(response: List<TradingData>): List<TradingData> =
                response

            override fun loadFromDb(): Flow<List<TradingData>> = flow {
                emit(tradingDataDao.getAll())
            }


            override fun saveToDb(data: List<TradingData>) {
                tradingDataDao.insertAll(data)
            }

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }

        }.asFlow()

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