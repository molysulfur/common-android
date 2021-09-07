package com.awonar.app.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.awonar.app.databinding.AwonarFragmentSettingBinding
import com.awonar.app.ui.setting.privacy.PrivacyActivity
import com.awonar.app.ui.setting.about.AboutMeActivity
import com.awonar.app.ui.setting.bank.BankAccountActivity
import com.awonar.app.ui.setting.experience.ExperienceActivity
import com.awonar.app.ui.setting.personal.PersonalInfoActivity
import com.molysulfur.library.extension.openActivity

class SettingFragment : Fragment() {

    private val binding: AwonarFragmentSettingBinding by lazy {
        AwonarFragmentSettingBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.awonarSettingButtonAbout.setOnClickListener { openActivity(AboutMeActivity::class.java) }
        binding.awonarSettingButtonPersonalInfo.setOnClickListener { openActivity(PersonalInfoActivity::class.java) }
        binding.awonarSettingButtonBankAccount.setOnClickListener { openActivity(BankAccountActivity::class.java) }
        binding.awonarSettingButtonPrivacy.setOnClickListener { openActivity(PrivacyActivity::class.java) }
        binding.awonarSettingButtonExperience.setOnClickListener { openActivity(ExperienceActivity::class.java) }
        return binding.root
    }


}