package com.awonar.android.shared.repos

import com.awonar.android.model.ExistsEmailResponse
import com.awonar.android.shared.api.UserService
import com.facebook.GraphRequest
import com.molysulfur.library.network.DirectNetworkFlow
import retrofit2.Response
import javax.inject.Inject
import android.os.Bundle
import com.awonar.android.model.user.User
import com.awonar.android.model.user.UserRequest
import com.awonar.android.model.user.UserResponse
import com.awonar.android.shared.db.hawk.UserPreferenceManager
import com.facebook.AccessToken
import com.facebook.GraphResponse
import com.facebook.HttpMethod
import com.molysulfur.library.network.NetworkFlow
import com.molysulfur.library.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRepository @Inject constructor(
    private val userService: UserService,
    private val preference: UserPreferenceManager
) {

    fun checkEmailExist(request: String?) =
        object : DirectNetworkFlow<String?, Boolean, ExistsEmailResponse>() {
            override fun createCall(): Response<ExistsEmailResponse> =
                userService.isExistsEmail(request).execute()

            override fun convertToResultType(response: ExistsEmailResponse): Boolean =
                response.statusCode == 200

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }

        }.asFlow()

    fun getUserInfoFromFacebook(): GraphResponse {
        val params = Bundle()
        params.putString("fields", "id,email")
        return GraphRequest(
            AccessToken.getCurrentAccessToken(),
            "/me",
            params,
            HttpMethod.GET
        ).executeAndWait()
    }

    fun getUser(request: UserRequest): Flow<Result<User?>> =
        object : NetworkFlow<UserRequest, User?, UserResponse>() {
            override fun shouldFresh(data: User?): Boolean = data != null || request.needFresh

            override fun createCall(): Response<UserResponse> = userService.getMe().execute()

            override fun convertToResultType(response: UserResponse): User = User(
                id = response.id,
                avatar = response.thumbnail,
                username = response.username,
                firstName = response.firstName,
                lastName = response.lastName,
                middleName = response.middleName,
                about = response.about,
                followerCount = response.totalFollower,
                followingCount = response.totalFollowing,
                copiesCount = response.totalTrade,
                isMe = true,
                accountVerifyType = User.getAccountVerifyType(response.accountVerify)
            )

            override fun loadFromDb(): Flow<User?> = flow {
                emit(preference.get())
            }

            override fun saveToDb(data: User?) {
                if (data != null)
                    preference.save(data)
            }

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }

        }.asFlow()

    fun getProfileUser(request: UserRequest): Flow<Result<User?>> =
        object : DirectNetworkFlow<UserRequest, User?, UserResponse>() {

            override fun createCall(): Response<UserResponse> =
                userService.getProfile(request.userId).execute()

            override fun convertToResultType(response: UserResponse): User = User(
                id = response.id,
                avatar = response.thumbnail,
                username = response.username,
                firstName = response.firstName,
                lastName = response.lastName,
                middleName = response.middleName,
                about = response.about,
                followerCount = response.totalFollower,
                followingCount = response.totalFollowing,
                copiesCount = response.totalTrade,
                isMe = false,
                accountVerifyType = null
            )

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }

        }.asFlow()


}