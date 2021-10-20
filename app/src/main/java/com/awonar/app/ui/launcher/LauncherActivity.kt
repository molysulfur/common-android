package com.awonar.app.ui.launcher

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.awonar.app.databinding.AwonarActivityLauncherBinding
import com.awonar.app.ui.auth.AuthViewModel
import com.awonar.app.ui.auth.AuthenticationActivity
import com.awonar.app.ui.main.MainActivity
import com.molysulfur.library.activity.BaseActivity
import com.molysulfur.library.extension.openActivityAndClearThisActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LauncherActivity : BaseActivity() {


    private val binding: AwonarActivityLauncherBinding by lazy {
        AwonarActivityLauncherBinding.inflate(layoutInflater)
    }

    private val viewModel: AuthViewModel by viewModels()
    private val launcherViewModel: LauncherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        lifecycleScope.launchWhenStarted {
            launch {
                launcherViewModel.loadConversionState.collect { _ ->
                    launcherViewModel.loadTradingData.collect { tradingData ->
                        if (tradingData != null) {
                            viewModel.autoSignIn.collect {
                                when (it) {
                                    false -> openActivityAndClearThisActivity(AuthenticationActivity::class.java)
                                    true -> openActivityAndClearThisActivity(MainActivity::class.java)
                                }
                            }
                        }
                    }

                }
            }
        }

    }
}