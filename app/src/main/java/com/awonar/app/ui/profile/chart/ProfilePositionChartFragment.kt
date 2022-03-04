package com.awonar.app.ui.profile.chart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.awonar.app.databinding.AwonarFragmentProfilePositionChartBinding
import com.awonar.app.ui.user.UserViewModel

class ProfilePositionChartFragment : Fragment() {

    private val userViewModel: UserViewModel by activityViewModels()
    private val viewModel: ProfileChartViewModel by activityViewModels()

    private val binding: AwonarFragmentProfilePositionChartBinding by lazy {
        AwonarFragmentProfilePositionChartBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding.viewModel = viewModel
        binding.userViewModel = userViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getExposure(userViewModel.userState.value)
    }
}