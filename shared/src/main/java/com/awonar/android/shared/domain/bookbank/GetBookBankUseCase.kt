package com.awonar.android.shared.domain.bookbank

import com.awonar.android.model.bookbank.BookBank
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.UserRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBookBankUseCase @Inject constructor(
    private val repository: UserRepository,
    @IoDispatcher val ioDispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, BookBank?>(ioDispatcher) {
    override fun execute(parameters: Unit): Flow<Result<BookBank?>> = repository.getBookBank()

}