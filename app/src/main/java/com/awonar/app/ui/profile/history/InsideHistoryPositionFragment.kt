package com.awonar.app.ui.profile.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.awonar.android.model.portfolio.PublicHistoryAggregate
import com.awonar.app.databinding.AwonarFragmentHistoryPortfolioBinding
import com.awonar.app.dialog.menu.MenuDialog
import com.awonar.app.dialog.menu.MenuDialogButtonSheet
import com.awonar.app.utils.ColorChangingUtil
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import timber.log.Timber

class InsideHistoryPositionFragment : Fragment() {

    companion object {
        private const val HISTORY_KEY = "history"
        private const val PORTFOLIO_KEY = "portfolio"
        private const val DIALOG_TAG = "com.awonar.app.ui.profile.inside.history.dialog.menu"
    }


    private val binding: AwonarFragmentHistoryPortfolioBinding by lazy {
        AwonarFragmentHistoryPortfolioBinding.inflate(layoutInflater)
    }

    private val viewModel: HistoryProfileViewModel by activityViewModels()
    private val args: InsideHistoryPositionFragmentArgs by navArgs()

    private val menuListener = object : MenuDialogButtonSheet.MenuDialogButtonSheetListener {
        override fun onMenuClick(menu: MenuDialog) {
            when (menu.key) {
                PORTFOLIO_KEY -> findNavController().navigate(
                    HistoryPositionFragmentDirections.historyPositionFragmentToPublicPortfolioFragment())
            }
        }

    }

    private val menuTimeListener = object : MenuDialogButtonSheet.MenuDialogButtonSheetListener {
        override fun onMenuClick(menu: MenuDialog) {
            viewModel.changeDateType(menu.key)
        }

    }

    private val dialog: MenuDialogButtonSheet by lazy {
        MenuDialogButtonSheet.Builder().apply {
            setMenus(arrayListOf(
                MenuDialog(key = PORTFOLIO_KEY, text = "Portfolio"),
                MenuDialog(key = HISTORY_KEY, text = "History")
            ))
            setListener(menuListener)
        }.build()
    }

    private val menuTime: MenuDialogButtonSheet by lazy {
        MenuDialogButtonSheet.Builder().apply {
            setMenus(arrayListOf(
                MenuDialog(key = "7D", text = "7D"),
                MenuDialog(key = "30D", text = "30D"),
                MenuDialog(key = "3M", text = "3M"),
                MenuDialog(key = "6M", text = "6M"),
                MenuDialog(key = "1Y", text = "1Y"),
                MenuDialog(key = "Today", text = "Today")
            ))
            setListener(menuTimeListener)
        }.build()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        launchAndRepeatWithViewLifecycle {
            viewModel.argregateState.collect { argregate ->
                argregate?.let {
                    setupArgregate(it)
                }
            }
        }
        launchAndRepeatWithViewLifecycle {
            viewModel.dateType.collect {
                viewModel.clear()
                viewModel.setSymbol(args.symbol)
                viewModel.getHistoryAggregate()
                viewModel.getHistoryPosition()
            }
        }
        binding.column1 = "Traders"
        binding.column2 = "Profitable"
        binding.column3 = "P/L(%)"
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.awoanrPortfolioHistoryButtonType.setOnClickListener {
            dialog.show(parentFragmentManager, DIALOG_TAG)
        }
        binding.awoanrPortfolioHistoryButtonCalendar.setOnClickListener {
            menuTime.show(parentFragmentManager, DIALOG_TAG)
        }
    }

    private fun setupArgregate(argregate: PublicHistoryAggregate) {
        binding.awoanrPortfolioHistoryVerticalTextTrades.setTitle("${argregate.tradeCount}")
        binding.awoanrPortfolioHistoryVerticalTextProfit.setTitle("%.2f%s".format(argregate.totalProfitabilityPercentage,
            "%"))
        binding.awoanrPortfolioHistoryVerticalTextPl.setTitle("%.2f%s".format(argregate.totalNetProfitPercentage,
            "%"))
        binding.awoanrPortfolioHistoryVerticalTextPl.setTitleTextColor(ColorChangingUtil.getTextColorChange(
            argregate.totalNetProfitPercentage))
    }

}