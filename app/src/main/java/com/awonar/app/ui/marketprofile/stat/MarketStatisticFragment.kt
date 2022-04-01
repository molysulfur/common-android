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
import com.awonar.app.ui.marketprofile.stat.financial.*
import com.awonar.app.ui.marketprofile.stat.overview.OverviewMarketAdapter
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import timber.log.Timber

class MarketStatisticFragment : Fragment() {

    private val viewModel: MarketProfileViewModel by activityViewModels()

    private val overviewMarketAdapter: OverviewMarketAdapter by lazy {
        OverviewMarketAdapter()
    }

    private val financialCardAdapter: FinancialCardAdapter by lazy {
        FinancialCardAdapter()
    }

    private val financialDropdownAdapter: FinancialDropDownAdapter by lazy {
        FinancialDropDownAdapter().apply {
            onDateSelect = {
                viewModel.setQuater(it)
            }
        }
    }

    private val financialAdapter: FinancialMarketAdapter by lazy {
        FinancialMarketAdapter(requireActivity()).apply {
            onItemSelected = { key ->
                viewModel.setBarEntry(key)
            }
        }
    }
    private val financialHeaderAdapter: FinancialHeaderAdapter by lazy {
        val itemLists = mutableListOf<FinancialMarketItem>()
        itemLists.add(FinancialMarketItem.TitleMarketItem("Financial Summary"))
        itemLists.add(FinancialMarketItem.ButtonGroupItem("annual",
            "quarter"))
        itemLists.add(FinancialMarketItem.TabsItem())
        FinancialHeaderAdapter().apply {
            onToggleButton = {
                viewModel.setQuaterType(it)
            }
            onSelected = { text ->
                text?.let {
                    viewModel.setFinancialTabType(it)
                }
            }

            itemList = itemLists
        }
    }

    private val concatAdapter: ConcatAdapter by lazy {
        val config = ConcatAdapter.Config.Builder().apply {
        }.build()
        ConcatAdapter(config,
            overviewMarketAdapter,
            financialCardAdapter,
            financialHeaderAdapter,
            financialDropdownAdapter,
            financialAdapter)
    }

    private val binding: AwonarFragmentMarketStatisticBinding by lazy {
        AwonarFragmentMarketStatisticBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        launchAndRepeatWithViewLifecycle {
            viewModel.dropdownList.collect { itemLists ->
                if (binding.awonarMarketStatisticRecycler.adapter == null) {
                    with(binding.awonarMarketStatisticRecycler) {
                        adapter = concatAdapter
                        layoutManager =
                            LinearLayoutManager(requireContext(),
                                LinearLayoutManager.VERTICAL,
                                false)
                    }
                }
                with(financialDropdownAdapter) {
                    itemList = itemLists
                }
            }
        }
        launchAndRepeatWithViewLifecycle {
            viewModel.cardItemList.collect { itemLists ->
                if (binding.awonarMarketStatisticRecycler.adapter == null) {
                    with(binding.awonarMarketStatisticRecycler) {
                        adapter = concatAdapter
                        layoutManager =
                            LinearLayoutManager(requireContext(),
                                LinearLayoutManager.VERTICAL,
                                false)
                    }
                }
                with(financialCardAdapter) {
                    itemList = itemLists
                }
            }
        }
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
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}