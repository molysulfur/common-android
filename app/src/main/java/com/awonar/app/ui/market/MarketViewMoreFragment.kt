package com.awonar.app.ui.market

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.awonar.app.databinding.AwonarFragmentMarketViewmoreBinding

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
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            it.getString("instrumentType")?.let { instrumentType ->
                viewModel.instrumentWithSector(instrumentType)
            }
        }
    }

}