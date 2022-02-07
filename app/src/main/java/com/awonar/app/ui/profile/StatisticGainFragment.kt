package com.awonar.app.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.awonar.app.databinding.AwonarFragmentProfileStatisticBinding
import com.awonar.app.databinding.AwonarFragmentStatisticGainBinding
import com.awonar.app.ui.user.UserViewModel
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect

class StatisticGainFragment : Fragment() {

    private val binding: AwonarFragmentStatisticGainBinding by lazy {
        AwonarFragmentStatisticGainBinding.inflate(layoutInflater)
    }

    private val userViewModel: UserViewModel by activityViewModels()
    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        launchAndRepeatWithViewLifecycle {

        }
        launchAndRepeatWithViewLifecycle {
            userViewModel.userState.collect { user ->
                if (user != null) {
                    user.id?.let { uid ->
                        profileViewModel.getGrowthStatistic(uid)
                    }
                }
            }
        }
        binding.viewModel = profileViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}