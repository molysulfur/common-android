package com.awonar.app.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.awonar.app.R
import com.awonar.app.databinding.AwonarFragmentProfileStatisticBinding
import com.awonar.app.ui.user.UserViewModel
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect


class StatisticProfileFragment : Fragment() {

    companion object {
        fun newInstance(): StatisticProfileFragment = StatisticProfileFragment()
    }

    private val binding: AwonarFragmentProfileStatisticBinding by lazy {
        AwonarFragmentProfileStatisticBinding.inflate(layoutInflater)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.supportFragmentManager?.commit {
            replace<StatisticGainFragment>(R.id.awonar_profile_statistic_layout_gain)
            replace<StatisticRiskFragment>(R.id.awonar_profile_statistic_layout_risk)
            replace<StatisticMostTradingFragment>(R.id.awonar_profile_statistic_layout_most_trading)
            setReorderingAllowed(true)
        }
    }
}