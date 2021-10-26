package com.awonar.app.ui.portfolio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.awonar.app.databinding.AwonarFragmentPortfolioBinding
import com.awonar.app.ui.market.MarketViewModel
import com.molysulfur.library.extension.openActivity
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PortFolioFragment : Fragment() {

    private val portViewModel: PortFolioViewModel by activityViewModels()
    private val marketViewModel: MarketViewModel by activityViewModels()

    private val binding: AwonarFragmentPortfolioBinding by lazy {
        AwonarFragmentPortfolioBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        launchAndRepeatWithViewLifecycle {
            launch {
                portViewModel.subscricbeQuote.collect { instrumentIds ->
                    instrumentIds.forEach {
                        marketViewModel.subscribe(it)
                    }
                }
            }
            launch {
                portViewModel.activedColumnState.collect { newColumn ->
                    if (newColumn.isNotEmpty()) {
                        binding.column1 = newColumn[0]
                        binding.column2 = newColumn[1]
                        binding.column3 = newColumn[2]
                        binding.column4 = newColumn[3]
                    }
                }
            }
        }
        binding.viewModel = portViewModel
        binding.marketViewModel = marketViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        marketViewModel.setNewQuoteListener()
        binding.awonarPortfolioImageIconList.setOnClickListener {
            openActivity(PortFolioColumnActivedActivity::class.java)
        }
        setColumnListener()
    }

    private fun setColumnListener() {
        binding.awonarPortfolioTextColumnOne.setOnClickListener {
            val tag = binding.awonarPortfolioTextColumnOne.tag
            portViewModel.sortColumn(1, tag == "DESC")
            binding.awonarPortfolioTextColumnOne.tag = if (tag == "DESC") "ASC" else "DESC"
        }
        binding.awonarPortfolioTextColumnTwo.setOnClickListener {
            val tag = binding.awonarPortfolioTextColumnTwo.tag
            portViewModel.sortColumn(1, tag == "DESC")
            binding.awonarPortfolioTextColumnTwo.tag = if (tag == "DESC") "ASC" else "DESC"
        }
        binding.awonarPortfolioTextColumnThree.setOnClickListener {
            val tag = binding.awonarPortfolioTextColumnThree.tag
            portViewModel.sortColumn(1, tag == "DESC")
            binding.awonarPortfolioTextColumnThree.tag = if (tag == "DESC") "ASC" else "DESC"
        }
        binding.awonarPortfolioTextColumnFore.setOnClickListener {
            val tag = binding.awonarPortfolioTextColumnFore.tag
            portViewModel.sortColumn(1, tag == "DESC")
            binding.awonarPortfolioTextColumnFore.tag = if (tag == "DESC") "ASC" else "DESC"
        }
    }

}