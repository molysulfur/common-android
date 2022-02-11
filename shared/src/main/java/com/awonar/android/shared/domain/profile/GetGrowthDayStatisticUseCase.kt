package com.awonar.android.shared.domain.profile

import com.awonar.android.model.user.DrawdownResponse
import com.awonar.android.model.user.StatGainDayRequest
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.ProfileRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGrowthDayStatisticUseCase @Inject constructor(
    private val repository: ProfileRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : FlowUseCase<StatGainDayRequest, HashMap<String, Float>?>(dispatcher) {
    override fun execute(parameters: StatGainDayRequest): Flow<Result<HashMap<String, Float>?>> =
        repository.getGrowthDayStatistic(parameters.uid, parameters.year)
}