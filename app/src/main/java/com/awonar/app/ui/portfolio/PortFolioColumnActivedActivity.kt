package com.awonar.app.ui.portfolio

import android.os.Bundle
import com.awonar.app.databinding.AwonarActivityPortfolioColumnActivedBinding
import com.molysulfur.library.activity.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PortFolioColumnActivedActivity : BaseActivity() {

    private val binding: AwonarActivityPortfolioColumnActivedBinding by lazy {
        AwonarActivityPortfolioColumnActivedBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}