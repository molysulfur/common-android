package com.awonar.app.ui.withdraw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.awonar.app.databinding.AwonarFragmentWithdrawMethodBinding
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect

class WithdrawMethodFragment : Fragment() {

    private val viewModel: WithdrawViewModel by activityViewModels()

    private val binding: AwonarFragmentWithdrawMethodBinding by lazy {
        AwonarFragmentWithdrawMethodBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        launchAndRepeatWithViewLifecycle {
            viewModel.navigationActions.collect {
                findNavController().navigate(it)
            }
        }
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}