package com.awonar.app.ui.socialtrade.filter

import android.os.Bundle
import com.awonar.app.databinding.AwonarActivitySocialTradeFilterBinding
import com.molysulfur.library.activity.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SocialTradeFilterActivity : BaseActivity() {

    private val binding: AwonarActivitySocialTradeFilterBinding by lazy {
        AwonarActivitySocialTradeFilterBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}