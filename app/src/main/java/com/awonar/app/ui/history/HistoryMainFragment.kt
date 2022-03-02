package com.awonar.app.ui.history

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.awonar.app.databinding.AwonarFragmentHistoryMainBinding
import com.awonar.app.dialog.menu.MenuDialog
import com.awonar.app.dialog.menu.MenuDialogButtonSheet
import com.awonar.app.ui.columns.ColumnsActivedActivity
import com.awonar.app.ui.columns.ColumnsViewModel
import com.awonar.app.ui.history.adapter.HistoryAdapter
import com.awonar.app.utils.ColorChangingUtil
import com.molysulfur.library.extension.openActivity
import com.molysulfur.library.extension.openActivityCompatForResult
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class HistoryMainFragment : Fragment() {

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
                    getHistory(timeStamp / 1000)
                }
            })
            .setMenus(menus)
            .build()
    }

    private val viewModel: HistoryViewModel by activityViewModels()
    private val columnsViewModel: ColumnsViewModel by activityViewModels()

    private val binding: AwonarFragmentHistoryMainBinding by lazy {
        AwonarFragmentHistoryMainBinding.inflate(layoutInflater)
    }

    private val activityResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK) {
                columnsViewModel.getActivedColumns()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        launchAndRepeatWithViewLifecycle {
            launch {
                columnsViewModel.activedColumnState.collect {
                    if (it.size >= 4) {
                        binding.awonarHistoryIncludeColumn.column1 = it[0]
                        binding.awonarHistoryIncludeColumn.column2 = it[1]
                        binding.awonarHistoryIncludeColumn.column3 = it[2]
                        binding.awonarHistoryIncludeColumn.column4 = it[3]
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
        launchAndRepeatWithViewLifecycle {
            viewModel.navigateAction.collect {
                findNavController().navigate(it)
            }
        }
        launchAndRepeatWithViewLifecycle {
            viewModel.aggregateState.collect { aggregate ->
                aggregate?.let {
                    binding.cashFlows = "$%.2f".format(
                        it.totalMoneyIn.plus(it.totalMoneyOut).minus(it.totalWithdrawalFees)
                    )
                    binding.totalFees = "$%.2f".format(it.totalFees)
                }
            }
        }
        launchAndRepeatWithViewLifecycle {
            columnsViewModel.activedColumnState.collect {
                if (it.size >= 4) {
                    (binding.awonarHistoryRecyclerItems.adapter as? HistoryAdapter)?.columns =
                        it
                }
            }
        }
        launchAndRepeatWithViewLifecycle {
            viewModel.historiesState.collect {
                if (binding.awonarHistoryRecyclerItems.adapter == null) {
                    binding.awonarHistoryRecyclerItems.apply {
                        layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        adapter = HistoryAdapter().apply {
                            onClick = {
                                viewModel.addHistoryDetail(it)
                                viewModel.navgiationTo(HistoryMainFragmentDirections.actionHistoryFragmentToHistoryDetailFragment())
                            }
                            onShowInsideInstrument = { a, b ->
                                viewModel.navgiationTo(HistoryMainFragmentDirections.actionHistoryFragmentToHistoryInsideFragment(
                                    null,
                                    a,
                                    viewModel.timeStamp.value
                                ))
                            }
                            onLoad = {
                                viewModel.getHistory()
                            }
                        }
                    }
                }
                (binding.awonarHistoryRecyclerItems.adapter as HistoryAdapter).itemLists =
                    it.toMutableList()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        columnsViewModel.setColumnType("history")
        binding.awonarHistoryCashContainer.setOnClickListener {
            openActivity(HistoryCashFlowActivity::class.java)
        }
        binding.awonarHistoryButtonFilter.setOnClickListener {
            if (filterDialog.isAdded) {
                filterDialog.dismiss()
            }
            filterDialog.show(childFragmentManager, MenuDialogButtonSheet.TAG)
        }
        binding.awonarHistoryButtonColumns.setOnClickListener {
            openActivityCompatForResult(
                launcher = activityResult,
                cls = ColumnsActivedActivity::class.java,
                bundle = bundleOf(ColumnsActivedActivity.EXTRA_COLUMNS_ACTIVED to "history")
            )
        }
        binding.awonarHistoryButtonType.setOnClickListener {
            if (binding.awonarHistoryButtonType.tag != "market") {
                binding.awonarHistoryButtonType.tag = "market"
            } else {
                binding.awonarHistoryButtonType.tag = "manual"
            }
            val prev7Day = Calendar.getInstance()
            prev7Day.add(Calendar.DATE, -7)
            val timestamp = prev7Day.timeInMillis / 1000
            getHistory(timestamp)
        }
    }

    private fun getHistory(timeStamp: Long) {
        when (binding.awonarHistoryButtonType.tag) {
            "market" -> {
                viewModel.getMarketHistory(timeStamp)
            }
            else -> {
                viewModel.getHistory(timeStamp / 1000)
                viewModel.getAggregate(timeStamp / 1000)
            }
        }
    }
}