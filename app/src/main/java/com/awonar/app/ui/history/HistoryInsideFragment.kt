package com.awonar.app.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.awonar.android.model.history.MarketHistory
import com.awonar.app.databinding.AwonarFragmentHistoryInsideInstrumentBinding
import com.awonar.app.ui.columns.ColumnsViewModel
import com.awonar.app.utils.ColorChangingUtil
import com.awonar.app.utils.ImageUtil
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect

class HistoryInsideFragment : Fragment() {

    private val viewModel: HistoryInsideViewModel by activityViewModels()
    private val columnsViewModel: ColumnsViewModel by activityViewModels()

    private val args: HistoryInsideFragmentArgs by navArgs()

    private val binding: AwonarFragmentHistoryInsideInstrumentBinding by lazy {
        AwonarFragmentHistoryInsideInstrumentBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        launchAndRepeatWithViewLifecycle {
            viewModel.navigationInsideChannel.collect {
                findNavController().navigate(it)
            }
        }
        launchAndRepeatWithViewLifecycle {
            columnsViewModel.activedColumnState.collect {
                if (it.size >= 4) {
                    binding.awonarHistoryInstideIncludeColumn.column1 = it[0]
                    binding.awonarHistoryInstideIncludeColumn.column2 = it[1]
                    binding.awonarHistoryInstideIncludeColumn.column3 = it[2]
                    binding.awonarHistoryInstideIncludeColumn.column4 = it[3]
                }
            }
        }
        launchAndRepeatWithViewLifecycle {
            viewModel.argreationHistroyState.collect { market ->
                market?.let {
                    if (it.master == null) {
                        setupSymbol(it)
                    } else {
                        setupCopies(it)
                    }
                }
            }
        }
        binding.viewModel = viewModel
        binding.columnsViewModel = columnsViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    private fun setupCopies(marketHistory: MarketHistory) {
        marketHistory.let {
            val totalInvested = it.totalInvested
            val pl = it.totalNetProfit
            val endValue = it.endEquity
            val plPercent = pl.times(100).div(totalInvested)
            binding.totalFees = "$%.2f".format(it.totalFees)
            ImageUtil.loadImage(binding.awoanrHistoryInsideImageLogo, it.picture)
            binding.awoanrHistoryInsideTextName.text = it.master?.username
            binding.awoanrHistoryInsideTextInvested.text = "$%.2f".format(totalInvested)
            binding.awoanrHistoryInsideTextPl.text = "$%.2f".format(pl)
            binding.awoanrHistoryInsideTextPl.setTextColor(
                ColorChangingUtil.getTextColorChange(pl)
            )
            binding.awoanrHistoryInsideTextTitlePl.text =
                "P/L %.2f%s".format(plPercent, "%")
            binding.awoanrHistoryInsideTextEndvalue.text = "$%.2f".format(endValue)
        }
    }

    private fun setupSymbol(marketHistory: MarketHistory) {
        marketHistory.let {
            val totalInvested = it.totalInvested
            val pl = it.totalNetProfit
            val endValue = it.endEquity
            val plPercent = pl.times(100).div(totalInvested)
            binding.totalFees = "$%.2f".format(it.totalFees)
            ImageUtil.loadImage(binding.awoanrHistoryInsideImageLogo, it.picture)
            binding.awoanrHistoryInsideTextName.text =
                it.symbol?.replace("BUY", "")?.replace("SELL", "")
            binding.awoanrHistoryInsideTextInvested.text = "$%.2f".format(totalInvested)
            binding.awoanrHistoryInsideTextPl.text = "$%.2f".format(pl)
            binding.awoanrHistoryInsideTextPl.setTextColor(
                ColorChangingUtil.getTextColorChange(pl)
            )
            binding.awoanrHistoryInsideTextTitlePl.text =
                "P/L %.2f%s".format(plPercent, "%")
            binding.awoanrHistoryInsideTextEndvalue.text = "$%.2f".format(endValue)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.timeStamp.value = args.timestamp
        args.symbol?.let {
            viewModel.getArgreation(it)
            viewModel.getHistoryInside(it)
        }
        args.copy?.let {
            viewModel.getCopiesHistory(it)
        }

        binding.awonarHistoryInsideToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

    }
}
