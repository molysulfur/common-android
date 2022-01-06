package com.awonar.android.shared.domain.profile

import com.awonar.android.model.user.User
import com.awonar.android.model.user.UserRequest
import com.awonar.android.shared.db.hawk.UserPreferenceManager
import com.awonar.android.shared.di.IoDispatcher
import com.awonar.android.shared.repos.UserRepository
import com.molysulfur.library.result.Result
import com.molysulfur.library.result.successOr
import com.molysulfur.library.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val repository: UserRepository,
    private val preference: UserPreferenceManager,
    @IoDispatcher val ioDispatcher: CoroutineDispatcher
) : FlowUseCase<UserRequest, User?>(ioDispatcher) {
    override fun execute(parameters: UserRequest): Flow<Result<User?>> = flow {
        val owner: User? = preference.get()
        var isMe = true
        val result = if (owner?.id == null || parameters.userId.equals(owner.id)) {
            repository.getUser(UserRequest(needFresh = false))
        } else {
            isMe = false
            repository.getProfileUser(parameters)
        }
        result.collect {
            val user = it.successOr(null)
            user?.isMe = isMe
            emit(Result.Success(user))
        }
    }
}