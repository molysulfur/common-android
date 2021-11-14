package com.awonar.app.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.awonar.app.databinding.AwonarFragmentHistoryBinding
import com.awonar.app.ui.history.adapter.HistoryAdapter
import com.awonar.app.utils.ColorChangingUtil
import com.molysulfur.library.utils.ColorUtils
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect

class HistoryFragment : Fragment() {

    private val viewModel: HistoryViewModel by activityViewModels()

    private val binding: AwonarFragmentHistoryBinding by lazy {
        AwonarFragmentHistoryBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launchAndRepeatWithViewLifecycle {
            viewModel.aggregateState.collect { aggregate ->
                aggregate?.let {
                    binding.startValue = "$%.2f".format(it.startEquity)
                    binding.endValue = "$%.2f".format(it.endEquity)
                    binding.moneyIn = "$%.2f".format(it.totalMoneyIn)
                    binding.moneyOut = "$%.2f".format(it.totalMoneyOut)
                    binding.profitLoss = "$%.2f".format(it.totalNetProfit)
                    binding.awonarHistoryTextProfitloss.setTextColor(
                        ColorChangingUtil.getTextColorChange(
                            requireContext(),
                            it.totalNetProfit
                        )
                    )
                }
            }
        }
        launchAndRepeatWithViewLifecycle {
            viewModel.historiesState.collect {
                if (binding.awonarHistoryRecyclerItems.adapter == null) {
                    binding.awonarHistoryRecyclerItems.apply {
                        adapter = HistoryAdapter()
                        layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                    }
                }
                (binding.awonarHistoryRecyclerItems.adapter as HistoryAdapter).apply {
                    submitData(it)
                }
            }
        }

    }

}