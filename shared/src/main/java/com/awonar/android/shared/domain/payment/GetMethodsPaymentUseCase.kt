package com.awonar.android.shared.domain.payment

import com.awonar.android.model.payment.MethodPayment
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.PaymentRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.result.successOr
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMethodsPaymentUseCase @Inject constructor(
    private val repository: PaymentRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<String, List<MethodPayment>>(dispatcher) {
    override fun execute(parameters: String): Flow<Result<List<MethodPayment>>> = flow {
        repository.getMethods(parameters).collect {
            val list: List<MethodPayment> = it.successOr(emptyList()) ?: emptyList()
            emit(Result.Success(list))
        }
    }
}