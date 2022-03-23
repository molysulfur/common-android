package com.awonar.app.ui.marketprofile.stat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.awonar.app.databinding.AwonarFragmentMarketStatisticBinding
import com.awonar.app.ui.marketprofile.MarketProfileViewModel
import com.awonar.app.ui.marketprofile.stat.financial.FinancialMarketAdapter
import com.awonar.app.ui.marketprofile.stat.financial.FinancialMarketItem
import com.awonar.app.ui.marketprofile.stat.overview.OverviewMarketAdapter
import com.github.mikephil.charting.data.BarEntry
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import timber.log.Timber

class MarketStatisticFragment : Fragment() {

    private val viewModel: MarketProfileViewModel by activityViewModels()

    private val overviewMarketAdapter: OverviewMarketAdapter by lazy {
        OverviewMarketAdapter()
    }

    private val financialAdapter: FinancialMarketAdapter by lazy {
        FinancialMarketAdapter(requireActivity()).apply {
            onDateSelect = {
                viewModel.setQuater(it)
            }
            onToggleButton = {
                viewModel.setQuaterType(it)
            }
            onSelected = { text ->
                text?.let {
                    viewModel.setFinancialTabType(it)
                }
            }
            onItemSelected = { entry ->
                viewModel.setBarEntry(entry)
            }
        }
    }

    private val concatAdapter: ConcatAdapter by lazy {
        val config = ConcatAdapter.Config.Builder().apply {
        }.build()
        ConcatAdapter(config, overviewMarketAdapter, financialAdapter)
    }

    private val binding: AwonarFragmentMarketStatisticBinding by lazy {
        AwonarFragmentMarketStatisticBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        launchAndRepeatWithViewLifecycle {
            viewModel.financialItemList.collect { itemLists ->
                if (binding.awonarMarketStatisticRecycler.adapter == null) {
                    with(binding.awonarMarketStatisticRecycler) {
                        adapter = concatAdapter
                        layoutManager =
                            LinearLayoutManager(requireContext(),
                                LinearLayoutManager.VERTICAL,
                                false)
                    }
                }
                with(financialAdapter) {
                    itemList = itemLists
                }
            }
        }
        launchAndRepeatWithViewLifecycle {
            viewModel.overviewMarketState.collect { itemLists ->
                if (binding.awonarMarketStatisticRecycler.adapter == null) {
                    with(binding.awonarMarketStatisticRecycler) {
                        adapter = concatAdapter
                        layoutManager =
                            LinearLayoutManager(requireContext(),
                                LinearLayoutManager.VERTICAL,
                                false)
                    }
                }
                with(overviewMarketAdapter) {
                    itemList = itemLists
                }
            }
        }
        launchAndRepeatWithViewLifecycle {
            viewModel.financalState.collect { response ->
                if (response != null) {
                    viewModel.convertFinancialToItem()
                }
            }
        }
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}