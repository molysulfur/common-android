package com.awonar.app.ui.trading

import android.os.Bundle
import com.awonar.app.databinding.AwonarActivityTradingBinding
import com.molysulfur.library.activity.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TradingActivity : BaseActivity() {

    private val binding: AwonarActivityTradingBinding by lazy {
        AwonarActivityTradingBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        setContentView(binding.root)
    }
}