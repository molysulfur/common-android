package com.awonar.android.shared.domain.tradingactivity

import com.awonar.android.model.tradingactivity.TradingActivityRequest
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.UserRepository
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class UpdateTradingActivityUseCase @Inject constructor(
    private val userRepository: UserRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : UseCase<TradingActivityRequest, Unit>(ioDispatcher) {
    override suspend fun execute(parameters: TradingActivityRequest) {
        userRepository.updateTradingActivity(parameters).collect {

        }
    }
}