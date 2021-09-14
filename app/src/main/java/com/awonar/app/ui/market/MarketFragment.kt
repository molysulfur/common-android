package com.awonar.app.ui.market

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarFragmentMarketBinding
import com.awonar.app.ui.market.adapter.InstrumentHorizontalAdapter
import com.awonar.app.ui.market.adapter.InstrumentHorizontalWrapperAdapter
import com.awonar.app.ui.market.adapter.InstrumentListAdapter

class MarketFragment : Fragment() {

    private val binding: AwonarFragmentMarketBinding by lazy {
        AwonarFragmentMarketBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ConcatAdapter(
            InstrumentHorizontalWrapperAdapter(InstrumentHorizontalAdapter()),
            InstrumentListAdapter()
        )
        binding.awonarMarketRecyclerInstrument.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        binding.awonarMarketRecyclerInstrument.adapter = adapter
    }
}