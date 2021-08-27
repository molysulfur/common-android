package com.awonar.android.shared.domain.user

import com.awonar.android.model.user.User
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.UserRepository
import com.molysulfur.library.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UpdateAboutMeUseCase @Inject constructor(
    private val repository: UserRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : UseCase<User, User>(ioDispatcher) {
    override suspend fun execute(parameters: User): User {
        repository.updateAboutMe(parameters).first()
        return parameters
    }
}