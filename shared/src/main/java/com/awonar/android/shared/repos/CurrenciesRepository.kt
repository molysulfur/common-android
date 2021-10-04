package com.awonar.android.shared.repos

import com.awonar.android.model.Conversion
import com.awonar.android.shared.api.CurrenciesService
import com.awonar.android.shared.db.room.conversionrate.ConversionRateDao
import com.molysulfur.library.network.NetworkFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class CurrenciesRepository @Inject constructor(
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
}