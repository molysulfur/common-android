package com.awonar.app.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.awonar.app.databinding.AwonarFragmentStatisticMostTradingBinding

class StatisticMostTradingFragment : Fragment() {


    private val binding: AwonarFragmentStatisticMostTradingBinding by lazy {
        AwonarFragmentStatisticMostTradingBinding.inflate(layoutInflater)
    }

    private val profileViewModel: ProfileViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding.viewModel = profileViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}