package com.awonar.android.shared.api

import com.awonar.android.model.ExistsEmailResponse
import com.awonar.android.model.core.MessageSuccessResponse
import com.awonar.android.model.tradingactivity.TradingActivityRequest
import com.awonar.android.model.user.PersonalInfoResponse
import com.awonar.android.model.user.UpdateAboutMeRequest
import com.awonar.android.model.user.UserResponse
import com.awonar.android.shared.constrant.BuildConfig
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface UserService {

    @GET("api/v1/verify/personal")
    fun getPersonalVerify() : Call<PersonalInfoResponse?>

    @PATCH("api/v1/users/settings")
    fun updateTradingActivity(@Body tradingActivity: TradingActivityRequest): Call<UserResponse>

    @GET("api/v1/users/email/{username}/exists")
    fun isExistsEmail(@Path("username") username: String?): Call<ExistsEmailResponse>

    @GET("api/v1/users/profile")
    fun getMe(): Call<UserResponse>

    @GET("api/v1/users/profile/{userId}")
    fun getProfile(@Path("userId") userId: String?): Call<UserResponse>

    @PUT("api/v1/users/about")
    fun updateAboutMe(@Body updateAboutMe: UpdateAboutMeRequest): Call<MessageSuccessResponse>

    companion object {

        fun create(client: NetworkClient): UserService = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client.getClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserService::class.java)

    }
}
