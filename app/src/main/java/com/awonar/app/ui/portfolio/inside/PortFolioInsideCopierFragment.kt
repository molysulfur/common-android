package com.awonar.app.ui.portfolio.inside

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.awonar.android.model.portfolio.Copier
import com.awonar.android.shared.steaming.QuoteSteamingManager
import com.awonar.android.shared.utils.ConverterQuoteUtil
import com.awonar.android.shared.utils.PortfolioUtil
import com.awonar.app.R
import com.awonar.app.databinding.AwonarFragmentPortfolioInsideCopierBinding
import com.awonar.app.dialog.menu.MenuDialog
import com.awonar.app.dialog.menu.MenuDialogButtonSheet
import com.awonar.app.ui.columns.ColumnsActivedActivity
import com.awonar.app.ui.columns.ColumnsViewModel
import com.awonar.app.ui.portfolio.PortFolioViewModel
import com.awonar.app.ui.portfolio.adapter.PortfolioItem
import com.awonar.app.ui.portfolio.position.PositionViewModel
import com.awonar.app.utils.ColorChangingUtil
import com.molysulfur.library.extension.openActivityCompatForResult
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

class PortFolioInsideCopierFragment : Fragment() {

    private val portFolioViewModel: PortFolioViewModel by activityViewModels()
    private val positionViewModel: PositionViewModel by activityViewModels()
    private val insideViewModel: PositionInsideViewModel by activityViewModels()
    private val columnsViewModel: ColumnsViewModel by activityViewModels()

    private val args: PortFolioInsideCopierFragmentArgs by navArgs()

    private var currentIndex: Int = 0

    private val binding: AwonarFragmentPortfolioInsideCopierBinding by lazy {
        AwonarFragmentPortfolioInsideCopierBinding.inflate(layoutInflater)
    }

    private val activityResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK) {
                columnsViewModel.getActivedColumns()
            }
        }
    private var menus: ArrayList<MenuDialog> = arrayListOf(
        MenuDialog(key = "add_fund", text = "Add Fund"),
        MenuDialog(key = "remove_fund", text = "Remove Fund"),
        MenuDialog(key = "pause_copy", text = "Pause Copy"),
        MenuDialog(key = "set_stoploss", text = "Set Stoploss Copy"),
        MenuDialog(key = "stop_copy", text = "Stop Copy"),
        MenuDialog(key = "customize", text = "Customize"),
        MenuDialog(key = "all_view", text = "All Trade View"),
    )

    private lateinit var settingBottomSheet: MenuDialogButtonSheet

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        launchAndRepeatWithViewLifecycle {
            launch {
                insideViewModel.sumFloatingPL.collectIndexed { _, value ->
                    binding.awonarPortfolioInsideCopierPositionHeader.setProfitLoss(value)
                }
            }
            launch {
                insideViewModel.sumValue.collectIndexed { _, value ->
                    binding.awonarPortfolioInsideCopierPositionHeader.setValueInvested(value)
                }
            }
            launch {
                QuoteSteamingManager.quotesState.collectIndexed { index, quotes ->
                    val item =
                        positionViewModel.positionItems.value[currentIndex] as PortfolioItem.CopierPortfolioItem
                    val copier = item.copier
                    var sumPL = 0f
                    copier.positions?.forEach { position ->
                        val quote = quotes[position.instrument?.id]
                        quote?.let {
                            val current = ConverterQuoteUtil.getCurrentPrice(
                                quote,
                                position.leverage,
                                position.isBuy
                            )
                            val pl = PortfolioUtil.getProfitOrLoss(
                                current,
                                position.openRate,
                                position.units,
                                position.conversionRate,
                                position.isBuy
                            )
                            sumPL += pl
                        }
                    }
                    val newPL = item.copier.closedPositionsNetProfit.plus(sumPL)
                    val money = copier.depositSummary.minus(item.copier.withdrawalSummary)
                    val value =
                        copier.initialInvestment.plus(money).plus(newPL)
                    binding.awonarPortfolioInsideCopierTextTotalOpenPl.apply {
                        text = "PL($): $%.2f".format(newPL)
                        setTextColor(ColorChangingUtil.getTextColorChange(requireContext(), newPL))

                    }
                    binding.awonarPortfolioInsideCopierTextTotalOpenPlPercent.apply {
                        text = "PL(%): " + "%.2f".format(
                            PortfolioUtil.profitLossPercent(
                                newPL,
                                copier.initialInvestment
                            )
                        )
                        setTextColor(
                            ColorChangingUtil.getTextColorChange(
                                requireContext(),
                                PortfolioUtil.profitLossPercent(
                                    newPL,
                                    copier.initialInvestment
                                )
                            )
                        )
                    }
                    binding.awonarPortfolioInsideCopierPositionHeader.apply {
                        setProfitLoss(newPL)
                        setValueInvested(value)
                    }

                }
            }
            launch {
                columnsViewModel.activedColumnState.collect { newColumn ->
                    if (newColumn.isNotEmpty()) {
                        binding.column1 = newColumn[0]
                        binding.column2 = newColumn[1]
                        binding.column3 = newColumn[2]
                        binding.column4 = newColumn[3]
                    }
                }
            }
        }
        binding.viewModel = insideViewModel
        binding.positionViewModel = positionViewModel
        binding.columnsViewModel = columnsViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentIndex = args.index
        columnsViewModel.getActivedColumns()
        insideViewModel.getCopiesWithIndex(
            portFolioViewModel.positionState.value,
            positionViewModel.positionItems.value[currentIndex] as PortfolioItem.CopierPortfolioItem
        )
        setupHeader()
        setupToolbar()
        setupFooter()
    }

    private fun setupHeader() {
        val item =
            positionViewModel.positionItems.value[currentIndex] as PortfolioItem.CopierPortfolioItem
        val copier = item.copier
        val invested = copier.initialInvestment
        val money = copier.depositSummary.minus(item.copier.withdrawalSummary)

        binding.awonarPortfolioInsideCopierPositionHeader.apply {
            setImage(copier.user.picture ?: "")
            setTitle(
                if (copier.user.private) copier.user.username
                    ?: "" else "%s %s".format(
                    copier.user.firstName,
                    copier.user.lastName
                )
            )

            setInvested(invested)
            setMoney(money)

        }
    }

    private fun setupFooter() {
        val item =
            positionViewModel.positionItems.value[currentIndex] as PortfolioItem.CopierPortfolioItem
        val copier = item.copier
        copier.let {
            binding.awonarPortfolioInsideCopierTextTotalOpenInvested.text =
                "Invested: $%.2f".format(it.investAmount)
            binding.awonarPortfolioInsideCopierTextTotalCloseFee.text =
                "Fees: $%.2f".format(it.totalFees)
            binding.awonarPortfolioInsideCopierTextTotalCloseNetprofit.text =
                "P/L($): $%.2f".format(it.closedPositionsNetProfit)
            binding.awonarPortfolioInsideCopierTextTotalCloseNetprofit.setTextColor(
                ColorChangingUtil.getTextColorChange(requireContext(), it.closedPositionsNetProfit)
            )
        }
    }

    private fun setupToolbar() {
        settingBottomSheet = MenuDialogButtonSheet.Builder()
            .setListener(object : MenuDialogButtonSheet.MenuDialogButtonSheetListener {
                override fun onMenuClick(menu: MenuDialog) {
                    when (menu.key) {
                        "add_fund" -> {
                            openAddFundDialog()
                        }
                        "remove_fund" -> {
                            openRemoveFundDialog()
                        }
                        "pause_copy" -> {
                            openPauseCopyDialog()
                        }
                        "stop_copy" -> {
                            openStopCopyDialog()
                        }
                        "set_stoploss" -> {
                            openEditCopyDialog()
                        }
                        "customize" -> {
                            openActivityCompatForResult(
                                activityResult,
                                ColumnsActivedActivity::class.java
                            )
                        }
                    }
                }
            })
            .setMenus(menus)
            .build()
        binding.awonarPortfolioInsideCopierPositionHeader.onSetting = {
            settingBottomSheet.show(childFragmentManager, "SettingButtonSheet")
        }
        binding.awonarPortfolioInsideInstrumentToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.awonarPortfolioInsideInstrumentToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.awonar_menu_instide_position_back -> {
                    if (currentIndex > 0) {
                        currentIndex--
//                        insideViewModel.getCopiesWithIndex(
//                            portFolioViewModel.positionState.value,
//                            currentIndex
//                        )
                    }
                    true
                }
                R.id.awonar_menu_instide_position_next -> {
                    val positionSize = portFolioViewModel.positionState.value?.copies?.size ?: 0
                    if (currentIndex < positionSize.minus(1)) {
                        currentIndex++
//                        insideViewModel.getCopiesWithIndex(
//                            portFolioViewModel.positionState.value,
//                            currentIndex
//                        )
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun openEditCopyDialog() {
//        EditCopierDialog.Builder()
//            .setCopies(copier = insideViewModel.copiesState.value)
//            .build()
//            .show(parentFragmentManager)
    }

    private fun openPauseCopyDialog() {
//        PauseCopierDialog.Builder()
//            .setCopies(copier = insideViewModel.copiesState.value)
//            .build()
//            .show(parentFragmentManager)
    }

    private fun openRemoveFundDialog() {
//        RemoveFundDialog.Builder()
//            .setCopies(copier = insideViewModel.copiesState.value)
//            .build()
//            .show(parentFragmentManager)
    }

    private fun openAddFundDialog() {
//        AddFundDialog.Builder()
//            .setCopies(copier = insideViewModel.copiesState.value)
//            .build()
//            .show(parentFragmentManager)
    }

    private fun openStopCopyDialog() {
//        StopCopierDialog.Builder()
//            .setCopies(copier = insideViewModel.copiesState.value)
//            .build()
//            .show(parentFragmentManager)
    }

}