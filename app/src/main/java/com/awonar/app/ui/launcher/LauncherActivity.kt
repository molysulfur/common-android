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
import timber.log.Timber

@AndroidEntryPoint
class LauncherActivity : BaseActivity() {


    private val binding: AwonarActivityLauncherBinding by lazy {
        AwonarActivityLauncherBinding.inflate(layoutInflater)
    }

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        lifecycleScope.launchWhenStarted {
            viewModel.autoSignIn.collect {
                Timber.e("$it")
                when (it) {
                    false -> openActivityAndClearThisActivity(AuthenticationActivity::class.java)
                    true -> openActivityAndClearThisActivity(MainActivity::class.java)
                }
            }
        }

    }
}