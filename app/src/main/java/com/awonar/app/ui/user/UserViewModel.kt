package com.awonar.app.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.bookbank.BookBank
import com.awonar.android.model.payment.MethodPayment
import com.awonar.android.model.settting.Bank
import com.awonar.android.model.user.User
import com.awonar.android.model.user.UserRequest
import com.awonar.android.shared.domain.bookbank.GetBookBankUseCase
import com.awonar.android.shared.domain.profile.GetUserProfileUseCase
import com.awonar.android.shared.domain.user.GetUserUseCase
import com.awonar.android.shared.domain.user.UpdateAboutMeUseCase
import com.awonar.android.shared.utils.WhileViewSubscribed
import com.molysulfur.library.result.data
import com.molysulfur.library.result.succeeded
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getBookBankUseCase: GetBookBankUseCase,
    private val updateAboutMeUseCase: UpdateAboutMeUseCase
) : ViewModel() {

    val bankState: StateFlow<BookBank?> =
        getBookBankUseCase(Unit).map { result ->
            result.data
        }.stateIn(viewModelScope, WhileViewSubscribed, null)

    private val _userState = MutableStateFlow<User?>(null)
    val userState: StateFlow<User?> get() = _userState

    fun getUser(needFresh: Boolean) {
        viewModelScope.launch {
            getUserUseCase(UserRequest(needFresh = needFresh)).collect {
                _userState.value = it.successOr(null)
            }
        }
    }


    fun getUser(userId: String) {
        viewModelScope.launch {
            getUserProfileUseCase(UserRequest(userId = userId)).collect {
                _userState.value = it.successOr(null)
            }
        }
    }

    fun updateUser(
        bio: String,
        about: String,
        skill: String,
        facebook: String,
        twitter: String,
        linkedIn: String,
        youtube: String,
        website: String
    ) {
        viewModelScope.launch {
            val user: User? = _userState.value
            val newUser: User? = user?.apply {
                this.bio = bio
                this.about = about
                this.skill = skill
                this.facebookLink = facebook
                this.twitterLink = twitter
                this.linkedInLink = linkedIn
                this.youtubeLink = youtube
                this.websiteLink = website
            }
            if (newUser != null) {
                val updateUser = updateAboutMeUseCase(newUser)
                if (updateUser.succeeded) {
                    _userState.value = updateUser.data
                }
            }
        }
    }


}