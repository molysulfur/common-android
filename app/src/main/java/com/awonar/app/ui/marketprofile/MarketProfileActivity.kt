package com.awonar.app.ui.marketprofile

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.awonar.android.model.market.Quote
import com.awonar.android.shared.steaming.QuoteSteamingManager
import com.awonar.android.shared.utils.ConverterQuoteUtil
import com.awonar.app.R
import com.awonar.app.databinding.AwonarActivityMarketProfileBinding
import com.awonar.app.ui.market.MarketViewModel
import com.awonar.app.utils.ColorChangingUtil
import com.molysulfur.library.activity.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MarketProfileActivity : BaseActivity() {

    private var instrumentId: Int = 0

    companion object {
        const val INSTRUMENT_EXTRA = "com.awonar.app.ui.marketprofile.extra.instrument_id"
    }

    private val marketViewModel: MarketViewModel by viewModels()
    private val viewModel: MarketProfileViewModel by viewModels()

    private val binding: AwonarActivityMarketProfileBinding by lazy {
        AwonarActivityMarketProfileBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setupViewPager()

        binding.awonarMarketProfileToolbarProfile.setNavigationOnClickListener {
            finish()
        }
        intent?.let {
            instrumentId = it.getIntExtra(INSTRUMENT_EXTRA, 0)
            if (instrumentId > 0) {
                viewModel.getInstrumentProfile(instrumentId)
                marketViewModel.subscribe(instrumentId)
            }
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                QuoteSteamingManager.quotesState.collect { quotes ->
                    val quote = quotes[instrumentId]
                    setChangeText(quote)
                }
            }
        }
    }

    private fun setupViewPager() {
        with(binding.awonarMarketProfileViewpager) {
            adapter = MarketProfilePagerAdapter(supportFragmentManager, lifecycle)
        }
    }

    private fun setChangeText(quote: Quote?) {
        val price = quote?.close ?: 0f
        val change: Float = ConverterQuoteUtil.change(quote?.close ?: 0f, quote?.previous ?: 0f)
        val percent: Float = ConverterQuoteUtil.percentChange(
            oldPrice = quote?.previous ?: 0f,
            newPrice = quote?.close ?: 0f
        )
        binding.awonarMarketProfileTextPricing.text =
            "%.${viewModel.instrumentState.value?.digit ?: 2}f".format(price)
        binding.awonarMarketProfileTextChange.setTextColor(
            ColorChangingUtil.getTextColorChange(
                this@MarketProfileActivity,
                change
            )
        )
        binding.awonarMarketProfileTextChange.text =
            "%.2f (%.2f%s)".format(change, percent, "%")
    }

}