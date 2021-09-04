package com.awonar.android.shared.repos

import com.awonar.android.model.privacy.PersonalCardIdRequest
import com.awonar.android.model.settting.Bank
import com.awonar.android.model.settting.Country
import com.awonar.android.model.user.PersonalInfoResponse
import com.awonar.android.shared.api.AuthService
import com.awonar.android.shared.api.RemoteConfigService
import com.awonar.android.shared.db.hawk.AccessTokenManager
import com.molysulfur.library.network.DirectNetworkFlow
import retrofit2.Response
import javax.inject.Inject

class RemoteConfigRepository @Inject constructor(
    private val remoteConfigService: RemoteConfigService
) {
    fun getCountriesList() = object :
        DirectNetworkFlow<Unit, List<Country>, List<Country>>() {
        override fun createCall(): Response<List<Country>> =
            remoteConfigService.countries().execute()

        override fun convertToResultType(response: List<Country>): List<Country> = response

        override fun onFetchFailed(errorMessage: String) {
            println(errorMessage)
        }

    }.asFlow()

    fun getBankLists() = object :
        DirectNetworkFlow<Unit, List<Bank>, List<Bank>>() {
        override fun createCall(): Response<List<Bank>> =
            remoteConfigService.banks().execute()

        override fun convertToResultType(response: List<Bank>): List<Bank> = response

        override fun onFetchFailed(errorMessage: String) {
            println(errorMessage)
        }

    }.asFlow()

}