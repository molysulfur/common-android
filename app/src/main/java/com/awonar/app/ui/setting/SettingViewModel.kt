package com.awonar.app.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.bookbank.BookBank
import com.awonar.android.model.settting.Country
import com.awonar.android.model.user.AccountVerifyType
import com.awonar.android.model.user.PersonalInfoResponse
import com.awonar.android.model.user.User
import com.awonar.android.shared.domain.personal.GetPersonalInfoUseCase
import com.awonar.android.shared.domain.profile.GetUserProfileUseCase
import com.awonar.android.shared.domain.remote.GetCountriesUseCase
import com.awonar.android.shared.domain.user.GetUserUseCase
import com.awonar.android.shared.utils.WhileViewSubscribed
import com.awonar.app.R
import com.awonar.app.ui.setting.about.AboutMeActivity
import com.awonar.app.ui.setting.adapter.SettingItem
import com.awonar.app.ui.setting.experience.ExperienceActivity
import com.awonar.app.ui.setting.personal.PersonalInfoActivity
import com.awonar.app.ui.setting.privacy.PrivacyActivity
import com.molysulfur.library.result.data
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
) : ViewModel() {

    private val _settingItemState = MutableStateFlow<List<SettingItem>>(emptyList())
    val settingItemState: StateFlow<List<SettingItem>> get() = _settingItemState

    fun convertUserToSettingItem(user: User?) {
        if (user != null) {
            val settingItemList = arrayListOf<SettingItem>()
            settingItemList.add(
                SettingItem(
                    buttonTextRes = R.string.awonar_text_about_me,
                    isAlert = false,
                    navigation = AboutMeActivity::class.java
                )
            )
            settingItemList.add(
                SettingItem(
                    buttonText = "Personal Information",
                    isAlert = user.accountVerifyType != AccountVerifyType.APPROVE,
                    navigation = PersonalInfoActivity::class.java
                )
            )
            settingItemList.add(
                SettingItem(
                    buttonText = "Bank",
                    isAlert = user.bankVerify != "approve",
                    navigation = PersonalInfoActivity::class.java
                )
            )
            settingItemList.add(
                SettingItem(
                    buttonText = "Privacy",
                    isAlert = false,
                    navigation = PrivacyActivity::class.java
                )
            )
            settingItemList.add(
                SettingItem(
                    buttonText = "Experience",
                    isAlert = false,
                    navigation = ExperienceActivity::class.java
                )
            )
            _settingItemState.value = settingItemList
        }
    }


}


