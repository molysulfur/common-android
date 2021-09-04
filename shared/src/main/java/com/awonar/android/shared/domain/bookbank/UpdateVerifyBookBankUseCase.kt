package com.awonar.android.shared.domain.bookbank

import com.awonar.android.model.bookbank.BookBank
import com.awonar.android.model.bookbank.BookBankRequest
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.UserRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateVerifyBookBankUseCase @Inject constructor(
    private val userRepository: UserRepository,
    @IoDispatcher val ioDispatcher: CoroutineDispatcher
) : FlowUseCase<BookBankRequest, BookBank?>(ioDispatcher) {
    override fun execute(parameters: BookBankRequest): Flow<Result<BookBank?>> =
        userRepository.updateBookBank(parameters)
}