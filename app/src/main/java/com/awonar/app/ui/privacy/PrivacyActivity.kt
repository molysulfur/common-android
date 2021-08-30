package com.awonar.app.ui.privacy

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.awonar.app.R
import com.awonar.app.databinding.AwonarActivityPrivacyBinding
import com.molysulfur.library.activity.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrivacyActivity : BaseActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val binding: AwonarActivityPrivacyBinding by lazy {
        AwonarActivityPrivacyBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        navController =
            (supportFragmentManager.findFragmentById(R.id.awonar_privacy_fragment_view) as NavHostFragment).navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.awonarPrivacyToolbar.setupWithNavController(navController, appBarConfiguration)
        binding.awonarPrivacyToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}