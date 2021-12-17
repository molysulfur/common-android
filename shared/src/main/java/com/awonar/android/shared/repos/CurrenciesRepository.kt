package com.awonar.android.shared.repos

import com.awonar.android.model.Conversion
import com.awonar.android.model.currency.Currency
import com.awonar.android.shared.api.CurrenciesService
import com.awonar.android.shared.db.room.conversionrate.ConversionRateDao
import com.molysulfur.library.network.DirectNetworkFlow
import com.molysulfur.library.network.NetworkFlow
import com.molysulfur.library.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

open class CurrenciesRepository @Inject constructor(
    private val conversionRateDao: ConversionRateDao,
    private val currenciesService: CurrenciesService
) {

    fun getConversionByInstrumentId(instrumentId: Int): Conversion =
        conversionRateDao.getById(instrumentId)

    fun getConversion(needFresh: Boolean) =
        object : NetworkFlow<Unit, List<Conversion>, List<Conversion>>() {
            override fun createCall(): Response<List<Conversion>> =
                currenciesService.getConversionRate().execute()

            override fun convertToResultType(response: List<Conversion>): List<Conversion> =
                response

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }

            override fun shouldFresh(data: List<Conversion>?): Boolean =
                data == null || needFresh

            override fun loadFromDb(): Flow<List<Conversion>> = flow {
                emit(conversionRateDao.getAll())
            }

            override fun saveToDb(data: List<Conversion>) {
                conversionRateDao.insertAll(data)
            }

        }.asFlow()

    fun getCurrencyRate(request: String): Flow<Result<Float?>> =
        object : DirectNetworkFlow<String, Float, Currency>() {
            override fun createCall(): Response<Currency> =
                currenciesService.getCurrencyRate(request).execute()

            override fun convertToResultType(response: Currency): Float = response.conversionUsdRate

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }

        }.asFlow()
}