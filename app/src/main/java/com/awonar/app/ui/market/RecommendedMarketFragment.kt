package com.awonar.app.ui.market

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.awonar.app.databinding.AwonarFragmentMarketRecommendedBinding
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect

class RecommendedMarketFragment : Fragment() {

    private val binding: AwonarFragmentMarketRecommendedBinding by lazy {
        AwonarFragmentMarketRecommendedBinding.inflate(layoutInflater)
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
        launchAndRepeatWithViewLifecycle {
            viewModel.marketTabState.collect { state ->
                when (state) {
                    MarketFragment.Companion.MarketTabSelectedState.TAB_SELECTED -> {
                    }
                    MarketFragment.Companion.MarketTabSelectedState.TAB_RESELECTED -> {
                        binding.awonarMarketRecyclerInstrument.smoothScrollToPosition(0)
                    }
                    MarketFragment.Companion.MarketTabSelectedState.TAB_UNSELECTED -> {
                    }
                }
            }
        }
    }

}