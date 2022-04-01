package com.awonar.app.ui.marketprofile.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.awonar.app.databinding.AwonarFragmentMarketAboutBinding
import com.awonar.app.ui.marketprofile.MarketProfileViewModel

class MarketAboutFragment : Fragment() {

    private val viewModel: MarketProfileViewModel by activityViewModels()

    private val binding: AwonarFragmentMarketAboutBinding by lazy {
        AwonarFragmentMarketAboutBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }


    companion object {

        fun newInstance(): MarketAboutFragment = MarketAboutFragment()
    }
}