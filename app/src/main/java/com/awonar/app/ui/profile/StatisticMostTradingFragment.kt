package com.awonar.app.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.awonar.app.databinding.AwonarFragmentStatisticMostTradingBinding
import com.awonar.app.ui.user.UserViewModel
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import java.util.*

class StatisticMostTradingFragment : Fragment() {

    private val binding: AwonarFragmentStatisticMostTradingBinding by lazy {
        AwonarFragmentStatisticMostTradingBinding.inflate(layoutInflater)
    }

    private val profileViewModel: ProfileViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        launchAndRepeatWithViewLifecycle {
            userViewModel.userState.collect { user ->
                if (user != null) {
                    user.id?.let { uid ->
                        profileViewModel.getMostTrading(uid)
                    }
                }
            }
        }
        binding.viewModel = profileViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}