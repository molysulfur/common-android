package com.awonar.app.ui.market

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.awonar.app.databinding.AwonarFragmentMarketRecommendedBinding
import com.awonar.app.ui.dialog.OrderDialog
import com.awonar.app.ui.market.adapter.InstrumentHorizontalAdapter
import com.awonar.app.ui.market.adapter.InstrumentHorizontalWrapperAdapter
import com.awonar.app.ui.market.adapter.InstrumentItem
import com.awonar.app.ui.market.adapter.InstrumentListAdapter
import com.awonar.app.ui.marketprofile.MarketProfileActivity
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect

class RecommendedMarketFragment : Fragment() {

    lateinit var horizontalAdapter: InstrumentHorizontalAdapter
    lateinit var instrumentAdapter: InstrumentListAdapter

    private val binding: AwonarFragmentMarketRecommendedBinding by lazy {
        AwonarFragmentMarketRecommendedBinding.inflate(layoutInflater)
    }

    private val viewModel: MarketViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setUpAdapter()
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launchAndRepeatWithViewLifecycle {
            viewModel.instrumentItem.collect { itemList ->
                instrumentAdapter.itemList = itemList
            }
        }
        launchAndRepeatWithViewLifecycle {
            viewModel.instruments.collect { instrument ->
                ((binding.awonarMarketRecyclerInstrument.adapter as? ConcatAdapter)?.adapters?.get(0) as InstrumentHorizontalWrapperAdapter).adapter.itemList =
                    instrument.filter { it.recommend }
                viewModel.convertInstrumentToItem()
            }
        }
        launchAndRepeatWithViewLifecycle {
            viewModel.marketTabState.collect { state ->
                when (state) {
                    MarketFragment.Companion.MarketTabSelectedState.TAB_SELECTED -> {
                    }
                    MarketFragment.Companion.MarketTabSelectedState.TAB_RESELECTED -> {
                        binding.awonarMarketRecyclerInstrument.smoothScrollToPosition(0)
                    }
                    MarketFragment.Companion.MarketTabSelectedState.TAB_UNSELECTED -> {
                    }
                }
            }
        }

    }

    private fun setUpAdapter() {
        val recyclerView = binding.awonarMarketRecyclerInstrument
        if (recyclerView.adapter == null) {
            horizontalAdapter = InstrumentHorizontalAdapter(viewModel)
            instrumentAdapter = InstrumentListAdapter(viewModel)
            horizontalAdapter.onInstrumentClick = {
                val newIntent = Intent(recyclerView.context, MarketProfileActivity::class.java)
                newIntent.putExtra(MarketProfileActivity.INSTRUMENT_EXTRA, it)
                recyclerView.context.startActivity(newIntent)
            }
            instrumentAdapter.onInstrumentClick = {
                val newIntent = Intent(recyclerView.context, MarketProfileActivity::class.java)
                newIntent.putExtra(MarketProfileActivity.INSTRUMENT_EXTRA, it)
                recyclerView.context.startActivity(newIntent)
            }
            instrumentAdapter.onViewMoreClick = { arg ->
                viewModel.onViewMore(arg)
            }
            instrumentAdapter.onOpenOrder = { instrument, type ->
                OrderDialog.Builder().setSymbol(instrument).build().show(childFragmentManager)
            }
            val adapter = ConcatAdapter(
                InstrumentHorizontalWrapperAdapter(horizontalAdapter),
                instrumentAdapter
            )
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
            recyclerView.adapter = adapter
        }
    }

}