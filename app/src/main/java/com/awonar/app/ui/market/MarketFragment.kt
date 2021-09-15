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
import com.awonar.app.databinding.AwonarFragmentMarketBinding
import com.awonar.app.ui.market.adapter.InstrumentHorizontalAdapter
import com.awonar.app.ui.market.adapter.InstrumentHorizontalWrapperAdapter
import com.awonar.app.ui.market.adapter.InstrumentListAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

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
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.awonarMarketTabCategory.addOnTabSelectedListener(this)
        binding.awonarMarketViewPagerInstrument.adapter = MarketPagerViewAdapter(this)
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