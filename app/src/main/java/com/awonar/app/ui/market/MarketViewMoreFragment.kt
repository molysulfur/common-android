package com.awonar.app.ui.market

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.awonar.app.databinding.AwonarFragmentMarketViewmoreBinding
import com.awonar.app.ui.market.adapter.InstrumentListAdapter
import com.awonar.app.ui.order.OrderDialog
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collectLatest

class MarketViewMoreFragment : Fragment() {

    private val binding: AwonarFragmentMarketViewmoreBinding by lazy {
        AwonarFragmentMarketViewmoreBinding.inflate(layoutInflater)
    }

    private val viewModel: MarketViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        launchAndRepeatWithViewLifecycle {
            viewModel.instrumentItem.collectLatest { instrument ->
                with(binding.awonarMarketListRecyclerItems) {
                    if (adapter == null) {
                        layoutManager =
                            LinearLayoutManager(
                                context,
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                        adapter = InstrumentListAdapter().apply {
                            onViewMoreClick = { arg ->
                                viewModel.onViewMore(arg)
                            }
                            onOpenOrder = { instrument, isBuy ->
                                OrderDialog.Builder()
                                    .setType(isBuy)
                                    .setSymbol(instrument)
                                    .build()
                                    .show(childFragmentManager)
                            }
                        }
                    }
                    (adapter as InstrumentListAdapter).itemList = instrument
                }
            }
        }
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val instrumentType = it.getString("instrumentType")
            val filterType = it.getString("filterType")
            if (instrumentType != null) {
                binding.awonarMarketViewMoreButtonSector.text = instrumentType
                when (filterType) {
                    "category" -> viewModel.instrumentWithCategory(instrumentType)
                    "sector" -> viewModel.instrumentWithSector(instrumentType)
                }
            }
        }
        binding.awonarMarketViewMoreButtonSector.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}