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

class MarketStatisticFragment : Fragment() {

    private val overviewMarketAdapter: OverviewMarketAdapter by lazy {
        OverviewMarketAdapter()
    }

    private val financialAdapter: FinancialMarketAdapter by lazy {
        FinancialMarketAdapter(requireActivity())
    }

    private val concatAdapter: ConcatAdapter by lazy {
        val config = ConcatAdapter.Config.Builder().apply {
            setIsolateViewTypes(false)
        }.build()
        ConcatAdapter(overviewMarketAdapter, financialAdapter)
    }

    private val viewModel: MarketProfileViewModel by activityViewModels()

    private val binding: AwonarFragmentMarketStatisticBinding by lazy {
        AwonarFragmentMarketStatisticBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
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
                val itemLists = mutableListOf<FinancialMarketItem>()
                itemLists.add(FinancialMarketItem.FinancialCardItem(
                    "Split Event",
                    response?.ststistics?.split?.historical?.get(0)?.date,
                    "%s:%s".format(response?.ststistics?.split?.historical?.get(0)?.denominator,
                        response?.ststistics?.split?.historical?.get(0)?.numerator)
                ))
                itemLists.add(FinancialMarketItem.FinancialCardItem(
                    "Divided",
                    response?.ststistics?.dividend?.historical?.get(0)?.date,
                    "%.2f".format(response?.ststistics?.dividend?.historical?.get(0)?.dividend)
                ))
                itemLists.add(FinancialMarketItem.TitleMarketItem("Financial Summary"))
                itemLists.add(FinancialMarketItem.ButtonGroupItem("annual", "quarter", ""))
//                itemLists.add(FinancialMarketItem.ViewPagerItem("annual"))
                /**
                 * income statistic
                 */
                val incomeGrossBarEntries = mutableListOf<BarEntry>()
                val incomeOperateBarEntries = mutableListOf<BarEntry>()
                response?.ststistics?.quarter?.forEachIndexed { index, financialQuarter ->
                    val grossY = if (financialQuarter.grossMargin == "N/A") {
                        0f
                    } else {
                        financialQuarter.grossMargin?.toFloat() ?: 0f
                    }
                    val operateY = financialQuarter.operatingMargin
                    incomeOperateBarEntries.add(BarEntry(index.toFloat(), operateY))
                    incomeGrossBarEntries.add(BarEntry(index.toFloat(), grossY))
                }
                itemLists.add(FinancialMarketItem.BarChartItem(
                    arrayListOf(
                        FinancialMarketItem.BarEntryItem(
                            "Gross Margin",
                            incomeGrossBarEntries
                        ),
                        FinancialMarketItem.BarEntryItem(
                            "Operating Margin",
                            incomeOperateBarEntries
                        ))
                ))
                /**
                 * Balanace Sheet
                 */
                val currentRatioEntries = mutableListOf<BarEntry>()
                response?.ststistics?.quarter?.forEachIndexed { index, financialQuarter ->
                    currentRatioEntries.add(BarEntry(index.toFloat(),
                        financialQuarter.currentRatio))
                }
                itemLists.add(FinancialMarketItem.BarChartItem(
                    arrayListOf(FinancialMarketItem.BarEntryItem(
                        "Current Ratio",
                        currentRatioEntries
                    ))))
                /**
                 * Cashflow
                 */
                val operateCashflow = mutableListOf<BarEntry>()
                response?.ststistics?.quarter?.forEachIndexed { index, financialQuarter ->
                    operateCashflow.add(BarEntry(index.toFloat(),
                        financialQuarter.operatingCashFlow))
                }
                itemLists.add(FinancialMarketItem.BarChartItem(
                    arrayListOf(
                        FinancialMarketItem.BarEntryItem(
                            "Operating Cashflow",
                            operateCashflow
                        )
                    )))
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
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}