package com.awonar.app.ui.marketprofile.stat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.awonar.app.R
import com.awonar.app.databinding.AwonarFragmentMarketStatisticBinding
import com.awonar.app.ui.profile.StatisticGainFragment
import com.awonar.app.ui.profile.StatisticMostTradingFragment
import com.awonar.app.ui.profile.StatisticRiskFragment

class MarketStatisticFragment : Fragment() {

    private val binding: AwonarFragmentMarketStatisticBinding by lazy {
        AwonarFragmentMarketStatisticBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.commit {
            replace<MarketOverviewFragment>(R.id.awonar_profile_statistic_layout_overview)
            replace<MarketDataFeedFragment>(R.id.awonar_profile_statistic_layout_chart)
            setReorderingAllowed(true)
        }
    }
}