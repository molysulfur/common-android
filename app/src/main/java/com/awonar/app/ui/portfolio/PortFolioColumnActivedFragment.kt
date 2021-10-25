package com.awonar.app.ui.portfolio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.awonar.app.databinding.AwonarFragmentPortfolioColumnActivedBinding

class PortFolioColumnActivedFragment : Fragment() {

    private val binding: AwonarFragmentPortfolioColumnActivedBinding by lazy {
        AwonarFragmentPortfolioColumnActivedBinding.inflate(layoutInflater)
    }

    private val viewModel: PortFolioViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

}