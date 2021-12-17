package com.awonar.android.shared.domain.payment

import com.awonar.android.model.payment.OTP
import com.awonar.android.model.payment.WithdrawOTPRequest
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.PaymentRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWithdrawOTPUseCase @Inject constructor(
    private val repository: PaymentRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<Float, OTP?>(dispatcher) {
    override fun execute(parameters: Float): Flow<Result<OTP?>> = repository.withdrawOTP(
        WithdrawOTPRequest(parameters)
    )
}