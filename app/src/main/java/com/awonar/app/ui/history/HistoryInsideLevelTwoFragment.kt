package com.awonar.app.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.awonar.app.databinding.AwonarFragmentHistoryInsideInstrumentBinding
import com.awonar.app.databinding.AwonarFragmentHistoryInstideLevelTwoBinding
import com.awonar.app.ui.columns.ColumnsViewModel
import com.awonar.app.ui.history.adapter.HistoryItem
import com.awonar.app.utils.ColorChangingUtil
import com.awonar.app.utils.ImageUtil
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HistoryInsideLevelTwoFragment : Fragment() {

    private val viewModel: HistoryInsideViewModel by activityViewModels()
    private val columnsViewModel: ColumnsViewModel by activityViewModels()

    private val args: HistoryInsideLevelTwoFragmentArgs by navArgs()

    private val binding: AwonarFragmentHistoryInstideLevelTwoBinding by lazy {
        AwonarFragmentHistoryInstideLevelTwoBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        launchAndRepeatWithViewLifecycle {
            columnsViewModel.activedColumnState.collect {
                if (it.size >= 4) {
                    binding.awonarHistoryInsideTwoIncludeColumn.column1 = it[0]
                    binding.awonarHistoryInsideTwoIncludeColumn.column2 = it[1]
                    binding.awonarHistoryInsideTwoIncludeColumn.column3 = it[2]
                    binding.awonarHistoryInsideTwoIncludeColumn.column4 = it[3]
                }
            }
        }
        launchAndRepeatWithViewLifecycle {
            launch {
                viewModel.argreationCopiesHistroyState.collect {
                    binding.invested = "$%.2f".format(it?.initialInvestment)
                    binding.moneyIn = "$%.2f".format(it?.depositSummary)
                    binding.moneyOut = "$%.2f".format(it?.withdrawalSummary)
                    binding.endValue = "$%.2f".format(it?.endEquity)
                    binding.profitLoss = "$%.2f".format(it?.totalNetProfit)
                    binding.totalFees = "$%.2f".format(it?.totalFees)
                    binding.awonarHistoryTextProfitloss.setTextColor(
                        ColorChangingUtil.getTextColorChange(
                            it?.totalNetProfit ?: 0f
                        )
                    )
                }
            }
        }
        binding.historyViewModel = viewModel
        binding.columnsViewModel = columnsViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.copy?.let {
            viewModel.getArgreationWithCopy(it)
            viewModel.getCopiesHistory(it, filter = "copy")
        }

    }
}
