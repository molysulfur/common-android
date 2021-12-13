package com.awonar.android.shared.domain.payment

import com.awonar.android.model.payment.DepositQrcodeRequest
import com.awonar.android.model.payment.QRCode
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.PaymentRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDepositQrcodeUseCase @Inject constructor(
    private val repository: PaymentRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<DepositQrcodeRequest, QRCode?>(dispatcher) {
    override fun execute(parameters: DepositQrcodeRequest): Flow<Result<QRCode?>> = repository.getDepositQrcode(parameters)
}

