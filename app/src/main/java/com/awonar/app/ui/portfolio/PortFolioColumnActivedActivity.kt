package com.awonar.app.ui.portfolio

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.awonar.app.R
import com.awonar.app.databinding.AwonarActivityPortfolioColumnActivedBinding
import com.molysulfur.library.activity.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PortFolioColumnActivedActivity : BaseActivity() {

    private val viewModel: PortFolioViewModel by viewModels()

    companion object {
        const val EXTRA_PORTFOLIO_TYPE =
            "com.awonar.app.ui.portfolio.actived.column.extra.portfolio_type"
    }

    private val binding: AwonarActivityPortfolioColumnActivedBinding by lazy {
        AwonarActivityPortfolioColumnActivedBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        intent.extras?.getString(EXTRA_PORTFOLIO_TYPE)?.let {
            viewModel.getActivedColoumn(it)
            viewModel.togglePortfolio(it)
        }
    }
}