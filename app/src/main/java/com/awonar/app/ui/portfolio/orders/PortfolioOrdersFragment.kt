package com.awonar.app.ui.portfolio.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.awonar.app.databinding.AwonarFragmentPortfolioOrdersBinding
import com.awonar.app.ui.portfolio.PortFolioViewModel

class PortfolioOrdersFragment : Fragment() {

    private val viewModel: PortFolioViewModel by activityViewModels()

    private val binding: AwonarFragmentPortfolioOrdersBinding by lazy {
        AwonarFragmentPortfolioOrdersBinding.inflate(layoutInflater)
    }

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
        viewModel.getOrdersPosition()
        viewModel.getActivedColoumn("manual")
    }
}