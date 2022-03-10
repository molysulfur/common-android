package com.awonar.app.ui.marketprofile.stat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.awonar.app.databinding.AwonarFragmentMarketOverviewBinding

class MarketOverviewFragment : Fragment() {
    private val binding: AwonarFragmentMarketOverviewBinding by lazy {
        AwonarFragmentMarketOverviewBinding.inflate(layoutInflater)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return binding.root
    }
}