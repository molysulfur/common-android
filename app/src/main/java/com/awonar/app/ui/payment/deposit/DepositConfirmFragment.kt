package com.awonar.app.ui.payment.deposit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.awonar.app.databinding.AwonarFragmentDepositConfirmBinding
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect

class DepositConfirmFragment : Fragment() {

    private val viewModel: DepositViewModel by activityViewModels()


    private val binding: AwonarFragmentDepositConfirmBinding by lazy {
        AwonarFragmentDepositConfirmBinding.inflate(layoutInflater)
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
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.accountCurrency = "USD"
        binding.depositCurreny = viewModel.symbolName.value
        binding.rate =
            "%s (USD/%s)".format(viewModel.currencyRate.value, viewModel.symbolName.value)
        binding.amount = "%.2f %s".format(viewModel.amount.value, viewModel.symbolName.value)
        binding.amountUsd =
            "%.2f USD".format(viewModel.amount.value.times(viewModel.currencyRate.value))
        binding.commission =
            "%s USD".format(viewModel.paymentSetting.value?.commissionDepositDollar)
        binding.awonarDepositConfirmButtonSubmit.setOnClickListener {
            viewModel.navigateQRCodeScanner()
        }
    }
}