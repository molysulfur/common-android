package com.awonar.android.shared.domain.market

import com.awonar.android.model.market.Instrument
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.steaming.QuoteSteamingEvent
import com.awonar.android.shared.steaming.QuoteSteamingManager
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class GetQuoteSteamingUseCase @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowUseCase<List<Instrument>, String>(dispatcher) {
    override fun execute(parameters: List<Instrument>): Flow<Result<String>> = flow {

    }

}