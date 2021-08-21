package com.awonar.android.shared.repos

import com.awonar.android.model.ExistsEmailResponse
import com.awonar.android.shared.api.UserService
import com.molysulfur.library.network.DirectNetworkFlow
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userService: UserService
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

}