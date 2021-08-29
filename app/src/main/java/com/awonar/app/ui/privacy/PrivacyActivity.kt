package com.awonar.app.ui.privacy

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.awonar.app.databinding.AwonarActivityPrivacyBinding
import com.molysulfur.library.activity.BaseActivity

class PrivacyActivity : BaseActivity() {
    private val binding: AwonarActivityPrivacyBinding by lazy {
        AwonarActivityPrivacyBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val navController = binding.awonarPrivacyFragmentView.findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
    }
}