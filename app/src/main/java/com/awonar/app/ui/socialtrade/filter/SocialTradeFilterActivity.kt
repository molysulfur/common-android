package com.awonar.app.ui.socialtrade.filter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.awonar.app.databinding.AwonarActivitySocialTradeFilterBinding
import com.awonar.app.ui.socialtrade.SocialTradeViewModel
import com.molysulfur.library.activity.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SocialTradeFilterActivity : BaseActivity() {

    private val viewModel: SocialTradeFilterViewModel by viewModels()
    private val socialViewModel: SocialTradeViewModel by viewModels()

    private val binding: AwonarActivitySocialTradeFilterBinding by lazy {
        AwonarActivitySocialTradeFilterBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.sendRequest.collect {
                    val newIntent = Intent().apply {
                        this.putExtra("map", it as HashMap)
                    }
                    setResult(Activity.RESULT_OK, newIntent)
                    finish()
                }
            }
        }
    }
}