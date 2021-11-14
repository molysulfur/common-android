package com.awonar.app.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.awonar.app.databinding.AwonarFragmentHistoryBinding
import com.awonar.app.dialog.menu.MenuDialog
import com.awonar.app.dialog.menu.MenuDialogButtonSheet
import com.awonar.app.ui.history.adapter.HistoryAdapter
import com.awonar.app.ui.portfolio.PortFolioFragmentDirections
import com.awonar.app.utils.ColorChangingUtil
import com.molysulfur.library.utils.ColorUtils
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class HistoryFragment : Fragment() {

    private val filterDialog: MenuDialogButtonSheet by lazy {
        val menus = arrayListOf(
            MenuDialog(
                key = "7D",
                text = "7D"
            ),
            MenuDialog(
                key = "30D",
                text = "30D"
            ),
            MenuDialog(
                key = "3M",
                text = "3M"
            ),
            MenuDialog(
                key = "6M",
                text = "6M"
            ),
            MenuDialog(
                key = "1Y",
                text = "1Y"
            ),
        )
        MenuDialogButtonSheet.Builder()
            .setListener(object : MenuDialogButtonSheet.MenuDialogButtonSheetListener {
                override fun onMenuClick(menu: MenuDialog) {
                    val prevTime = Calendar.getInstance()
                    val timeStamp: Long = when (menu.key) {
                        "30D" -> {
                            prevTime.add(Calendar.DATE, -30)
                            prevTime.timeInMillis
                        }
                        "3M" -> {
                            prevTime.add(Calendar.MONTH, -3)
                            prevTime.timeInMillis
                        }
                        "6M" -> {
                            prevTime.add(Calendar.MONTH, -6)
                            prevTime.timeInMillis
                        }
                        "1Y" -> {
                            prevTime.add(Calendar.YEAR, -1)
                            prevTime.timeInMillis
                        }
                        else -> {
                            prevTime.add(Calendar.DATE, -7)
                            prevTime.timeInMillis
                        }
                    }
                    viewModel.getHistory(timeStamp / 1000)
                    viewModel.getAggregate(timeStamp / 1000)
                }
            })
            .setMenus(menus)
            .build()
    }


    private val viewModel: HistoryViewModel by activityViewModels()

    private val binding: AwonarFragmentHistoryBinding by lazy {
        AwonarFragmentHistoryBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        launchAndRepeatWithViewLifecycle {
            launch {
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
            launch {
                viewModel.aggregateState.collect { aggregate ->
                    aggregate?.let {
                        binding.startValue = "$%.2f".format(it.startEquity)
                        binding.endValue = "$%.2f".format(it.endEquity)
                        binding.moneyIn = "$%.2f".format(it.totalMoneyIn)
                        binding.moneyOut = "$%.2f".format(it.totalMoneyOut)
                        binding.profitLoss = "$%.2f".format(it.totalNetProfit)
                        binding.cashFlows = "$%.2f".format(
                            it.totalMoneyIn.plus(it.totalMoneyOut).minus(it.totalWithdrawalFees)
                        )
                        binding.totalFees = "$%.2f".format(it.totalFees)
                        binding.awonarHistoryTextProfitloss.setTextColor(
                            ColorChangingUtil.getTextColorChange(
                                requireContext(),
                                it.totalNetProfit
                            )
                        )
                    }
                }
            }
        }
        binding.historyViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.awonarHistoryButtonFilter.setOnClickListener {
            filterDialog.show(childFragmentManager, MenuDialogButtonSheet.TAG)
        }
    }

}