package com.awonar.app.ui.setting.personal

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.awonar.app.databinding.AwonarActivityPersonalInfoBinding
import com.awonar.app.ui.setting.privacy.PrivacyViewModel
import com.molysulfur.library.activity.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class PersonalInfoActivity : BaseActivity() {


    private val binding: AwonarActivityPersonalInfoBinding by lazy {
        AwonarActivityPersonalInfoBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}