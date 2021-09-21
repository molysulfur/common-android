package com.awonar.app.ui.market

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.awonar.android.model.market.MarketViewMoreArg
import com.awonar.android.model.market.Quote
import com.awonar.android.shared.steaming.QuoteSteamingListener
import com.awonar.android.shared.steaming.QuoteSteamingManager
import com.awonar.app.R
import com.awonar.app.databinding.AwonarFragmentMarketBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MarketFragment : Fragment(), TabLayout.OnTabSelectedListener {

    companion object {
        enum class MarketTabSelectedState {
            TAB_SELECTED,
            TAB_UNSELECTED,
            TAB_RESELECTED
        }
    }

    private val binding: AwonarFragmentMarketBinding by lazy {
        AwonarFragmentMarketBinding.inflate(layoutInflater)
    }

    private val viewModel: MarketViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        launchAndRepeatWithViewLifecycle {
            viewModel.instrumentItem.collect {
                viewModel.subscribe()
            }
        }
        launchAndRepeatWithViewLifecycle {
            viewModel.viewMoreState.collect { instrumentType ->
                instrumentType?.let {
                    findNavController().navigate(
                        R.id.action_marketFragment_to_marketViewMoreFragment,
                        bundleOf(
                            "instrumentType" to instrumentType.instrumentType,
                            "filterType" to instrumentType.filterType
                        )
                    )
                }
            }
        }
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.setNewQuoteListener()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
    }

    fun initViewPager() {
        binding.awonarMarketTabCategory.addOnTabSelectedListener(this)
        binding.awonarMarketViewPagerInstrument.apply {
            isSaveEnabled = false
            adapter = MarketPagerViewAdapter(this@MarketFragment)
            isUserInputEnabled = false
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    when (position) {
                        1 -> viewModel.convertInstrumentCategoryToItem()
                        2 -> viewModel.convertInstrumentCryptoToItem()
                        3 -> viewModel.convertInstrumentCurrenciesToItem()
                        4 -> viewModel.convertInstrumentETFsToItem()
                        5 -> viewModel.convertInstrumentCommodityToItem()
                    }
                }
            })
        }
        TabLayoutMediator(
            binding.awonarMarketTabCategory,
            binding.awonarMarketViewPagerInstrument
        ) { tab, position ->
            tab.text = MarketPagerViewAdapter.tabLists[position]
        }.attach()
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        viewModel.tabMarketStateChange(MarketTabSelectedState.TAB_SELECTED)
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        viewModel.tabMarketStateChange(MarketTabSelectedState.TAB_UNSELECTED)
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
        viewModel.tabMarketStateChange(MarketTabSelectedState.TAB_RESELECTED)
    }

}