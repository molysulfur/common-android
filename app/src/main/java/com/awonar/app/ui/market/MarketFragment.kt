package com.awonar.app.ui.market

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.awonar.android.shared.steaming.QuoteSteamingManager
import com.awonar.app.databinding.AwonarFragmentMarketBinding
import com.awonar.app.ui.market.adapter.InstrumentHorizontalAdapter
import com.awonar.app.ui.market.adapter.InstrumentHorizontalWrapperAdapter
import com.awonar.app.ui.market.adapter.InstrumentListAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
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
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    fun initViewPager() {
        binding.awonarMarketTabCategory.addOnTabSelectedListener(this)
        binding.awonarMarketViewPagerInstrument.apply {
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