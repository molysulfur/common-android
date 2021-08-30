package com.awonar.app.ui.privacy

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.awonar.app.R
import com.awonar.app.databinding.AwonarActivityPrivacyBinding
import com.molysulfur.library.activity.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrivacyActivity : BaseActivity() {

    private val binding: AwonarActivityPrivacyBinding by lazy {
        AwonarActivityPrivacyBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.awonarPrivacyToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}