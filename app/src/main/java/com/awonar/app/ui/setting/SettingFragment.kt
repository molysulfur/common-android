package com.awonar.app.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.awonar.app.databinding.AwonarFragmentSettingBinding
import com.awonar.app.ui.setting.privacy.PrivacyActivity
import com.awonar.app.ui.setting.about.AboutMeActivity
import com.awonar.app.ui.setting.bank.BankAccountActivity
import com.awonar.app.ui.setting.experience.ExperienceActivity
import com.awonar.app.ui.setting.personal.PersonalInfoActivity
import com.awonar.app.ui.user.UserViewModel
import com.molysulfur.library.extension.openActivity
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect

class SettingFragment : Fragment() {

    private val userViewModel: UserViewModel by activityViewModels()
    private val settingViewModel: SettingViewModel by activityViewModels()

    private val binding: AwonarFragmentSettingBinding by lazy {
        AwonarFragmentSettingBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        launchAndRepeatWithViewLifecycle {
            userViewModel.userState.collect {
                settingViewModel.convertUserToSettingItem(it)
            }
        }

        binding.viewModel = settingViewModel
        return binding.root
    }


}