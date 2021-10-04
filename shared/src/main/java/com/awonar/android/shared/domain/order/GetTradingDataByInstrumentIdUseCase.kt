package com.awonar.android.shared.domain.order

import com.awonar.android.model.tradingdata.TradingData
import com.awonar.android.shared.db.room.trading.TradingDataDao
import com.awonar.android.shared.di.IoDispatcher
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class GetTradingDataByInstrumentIdUseCase @Inject constructor(
    private val tradingDataDao: TradingDataDao,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<Int, TradingData>(dispatcher) {
    override fun execute(parameters: Int): Flow<Result<TradingData>> = flow {
        emit(
            Result.Success(
                tradingDataDao.loadById(parameters)
            )
        )
    }
}