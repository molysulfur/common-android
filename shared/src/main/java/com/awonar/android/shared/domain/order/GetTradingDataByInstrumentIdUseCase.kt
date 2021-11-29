package com.awonar.android.shared.domain.order

import com.awonar.android.model.tradingdata.TradingData
import com.awonar.android.shared.db.room.trading.TradingDataDao
import com.awonar.android.shared.di.IoDispatcher
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetTradingDataByInstrumentIdUseCase @Inject constructor(
    private val tradingDataDao: TradingDataDao,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<Int, TradingData>(dispatcher) {
    override suspend fun execute(parameters: Int): TradingData {
        return tradingDataDao.loadById(parameters)
    }


}