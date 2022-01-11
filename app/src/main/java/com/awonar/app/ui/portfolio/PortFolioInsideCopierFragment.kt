package com.awonar.app.ui.portfolio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.awonar.app.databinding.AwonarFragmentPortfolioInsideCopierBinding
import com.awonar.app.dialog.DialogViewModel
import com.awonar.app.dialog.copier.add.AddFundDialog
import com.awonar.app.dialog.copier.pause.PauseCopierDialog
import com.awonar.app.dialog.copier.remove.RemoveFundDialog
import com.awonar.app.dialog.copier.stop.StopCopierDialog
import com.awonar.app.dialog.copier.stop.StopCopierListener
import com.awonar.app.dialog.menu.MenuDialog
import com.awonar.app.dialog.menu.MenuDialogButtonSheet
import com.awonar.app.ui.columns.ColumnsViewModel
import com.awonar.app.ui.market.MarketViewModel
import com.molysulfur.library.extension.toast
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class PortFolioInsideCopierFragment : Fragment() {

    private val portFolioViewModel: PortFolioViewModel by activityViewModels()
    private val columnsViewModel: ColumnsViewModel by activityViewModels()
    private val marketViewModel: MarketViewModel by activityViewModels()

    private val dialogViewModel: DialogViewModel by lazy {
        ViewModelProvider(this).get(DialogViewModel::class.java)
    }

    private val args: PortFolioInsideCopierFragmentArgs by navArgs()

    private val binding: AwonarFragmentPortfolioInsideCopierBinding by lazy {
        AwonarFragmentPortfolioInsideCopierBinding.inflate(layoutInflater)
    }

    private var menus: ArrayList<MenuDialog> = arrayListOf(
        MenuDialog(key = "add_fund", text = "Add Fund"),
        MenuDialog(key = "remove_fund", text = "Remove Fund"),
        MenuDialog(key = "pause_copy", text = "Pause Copy"),
        MenuDialog(key = "set_stoploss", text = "Set Stoploss Copy"),
        MenuDialog(key = "stop_copy", text = "Stop Copy"),
    )

    private lateinit var settingBottomSheet: MenuDialogButtonSheet


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        launchAndRepeatWithViewLifecycle {
            dialogViewModel.stopCopierDialog.observe(viewLifecycleOwner,
                object : StopCopierListener {
                    override fun onSuccess(message: String) {
                        toast(message)
                    }

                })
        }
        launchAndRepeatWithViewLifecycle {
            portFolioViewModel.navigateInsideInstrumentPortfolio.collect { pair ->
                val copier = portFolioViewModel.copierState.value
                copier?.let {
                    val position = copier.positions?.find { it.id == pair.first }
                    when (pair.second) {
                        "instrument" -> findNavController()
                            .navigate(
                                PortFolioInsideCopierFragmentDirections.actionPortFolioInsideCopierFragmentToPortFolioInsideInstrumentProfileFragment()
                                    .apply {
                                        this.copier = copier.id
                                        this.instrumentId = position?.instrumentId ?: 0
                                    }
                            )
                    }
                }

            }
        }
        launchAndRepeatWithViewLifecycle {
            launch {
                portFolioViewModel.copierState.collect {
                    marketViewModel.getConversionsRateList(it?.positions ?: emptyList())
                    columnsViewModel.getActivedColumns()
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
        binding.viewModel = portFolioViewModel
        binding.columnsViewModel = columnsViewModel
        binding.marketViewModel = marketViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                    }
                }
            })
            .setMenus(menus)
            .build()
        columnsViewModel.setColumnType("market")
        args.positionId.let {
            portFolioViewModel.getCopierPosition(it)
        }

        binding.awonarPortfolioInsideCopierPositionHeader.onSetting = {
            settingBottomSheet.show(childFragmentManager, "SettingButtonSheet")
        }
    }

    private fun openPauseCopyDialog() {
        PauseCopierDialog.Builder()
            .setCopies(copier = portFolioViewModel.copierState.value)
            .build()
            .show(parentFragmentManager)
    }

    private fun openRemoveFundDialog() {
        RemoveFundDialog.Builder()
            .setCopies(copier = portFolioViewModel.copierState.value)
            .build()
            .show(parentFragmentManager)
    }

    private fun openAddFundDialog() {
        AddFundDialog.Builder()
            .setCopies(copier = portFolioViewModel.copierState.value)
            .build()
            .show(parentFragmentManager)
    }

    private fun openStopCopyDialog() {
        StopCopierDialog.Builder()
            .setCopies(copier = portFolioViewModel.copierState.value)
            .build()
            .show(parentFragmentManager)
    }
}