package com.awonar.app.ui.payment.withdraw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.awonar.app.databinding.AwonarFragmentWithdrawHistoryBinding

class WithdrawHistoryFragment : Fragment() {

    private val binding: AwonarFragmentWithdrawHistoryBinding by lazy {
        AwonarFragmentWithdrawHistoryBinding.inflate(layoutInflater)
    }

    private val viewModel: WithdrawViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getHistory()
    }
}