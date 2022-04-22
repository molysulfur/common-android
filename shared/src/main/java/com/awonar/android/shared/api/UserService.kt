package com.awonar.android.shared.api

import com.awonar.android.model.ExistsEmailResponse
import com.awonar.android.model.bookbank.BookBank
import com.awonar.android.model.bookbank.BookBankRequest
import com.awonar.android.model.core.MessageSuccessResponse
import com.awonar.android.model.experience.ExperienceAnswerResponse
import com.awonar.android.model.experience.ExperienceRequest
import com.awonar.android.model.experience.ExperienceResponse
import com.awonar.android.model.feed.UserTag
import com.awonar.android.model.privacy.PersonalAddressRequest
import com.awonar.android.model.privacy.PersonalCardIdRequest
import com.awonar.android.model.privacy.PersonalProfileRequest
import com.awonar.android.model.tradingactivity.TradingActivityRequest
import com.awonar.android.model.user.*
import com.awonar.android.shared.constrant.BuildConfig
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface UserService {

    @GET("v1/search/users/{keyword}")
    fun searchUsersWithKeyword(
        @Path("keyword") keyword: String,
    ): Call<List<UserTag>?>

    @POST("v1/follow/{uid}")
    fun isFollow(@Path("uid") userId: String?, @Body isUser: IsFollowUser): Call<FollowResponse?>


    @GET("v1/questionnaires/user")
    fun getUserAnswer(@Query("questionnaireId") questionnaireId: String): Call<ExperienceAnswerResponse?>

    @PUT("v1/questionnaires")
    fun updateExperience(@Body experienceRequest: ExperienceRequest): Call<ExperienceAnswerResponse?>

    @GET("v1/questionnaires/experience")
    fun getExperience(): Call<ExperienceResponse?>

    @GET("v1/verify/bank")
    fun getBookBank(): Call<BookBank?>

    @POST("v1/verify/bank")
    fun verifyBookBank(@Body verify: BookBankRequest): Call<BookBank?>

    @POST("v1/verify/personal/identity")
    fun verifyCardId(@Body verify: PersonalCardIdRequest): Call<PersonalInfoResponse?>

    @POST("v1/verify/personal/contact")
    fun verifyAddress(@Body verify: PersonalAddressRequest): Call<PersonalInfoResponse?>

    @POST("v1/verify/personal/info")
    fun verifyProfile(@Body verify: PersonalProfileRequest): Call<PersonalInfoResponse?>

    @GET("v1/verify/personal")
    fun getPersonalVerify(): Call<PersonalInfoResponse?>

    @PATCH("v1/users/settings")
    fun updateTradingActivity(@Body tradingActivity: TradingActivityRequest): Call<UserResponse>

    @GET("v1/users/email/{username}/exists")
    fun isExistsEmail(@Path("username") username: String?): Call<ExistsEmailResponse>

    @GET("v1/users/profile")
    fun getMe(): Call<UserResponse>

    @GET("v1/users/profile/{userId}")
    fun getProfile(@Path("userId") userId: String?): Call<UserResponse>

    @PUT("v1/users/about")
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
