package com.awonar.app.ui.portfolio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.awonar.app.databinding.AwonarFragmentPortfolioManualBinding
import com.awonar.app.ui.market.MarketViewModel
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioAdapter
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PortFolioMaunalFragment : Fragment() {

    private val viewModel: PortFolioViewModel by activityViewModels()
    private val marketViewModel: MarketViewModel by activityViewModels()

    private val binding: AwonarFragmentPortfolioManualBinding by lazy {
        AwonarFragmentPortfolioManualBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        launchAndRepeatWithViewLifecycle {
            launch {
                viewModel.sortColumnState.collect {
                    if (binding.awonarPortfolioRecyclerOrders.adapter != null) {
                        val adapter: OrderPortfolioAdapter =
                            binding.awonarPortfolioRecyclerOrders.adapter as OrderPortfolioAdapter
                        adapter.sortColumn(it.first, it.second)
                    }
                }
            }
        }
        binding.viewModel = viewModel
        binding.marketViewModel = marketViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}