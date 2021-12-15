package com.awonar.app.ui.withdraw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.awonar.app.databinding.AwonarFragmentWithdrawBankingBinding
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import java.lang.NumberFormatException

class WithdrawBankingFragment : Fragment() {

    private val binding: AwonarFragmentWithdrawBankingBinding by lazy {
        AwonarFragmentWithdrawBankingBinding.inflate(layoutInflater)
    }

    private val viewModel: WithdrawViewModel by activityViewModels()
    private val args: WithdrawBankingFragmentArgs by navArgs()

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
        launchAndRepeatWithViewLifecycle {
            viewModel.portfolioState.collect { portfolio ->
                binding.balance = "$%.2f".format(portfolio?.available ?: 0f)
                binding.total = "$%.2f".format(
                    portfolio?.available?.plus(portfolio.totalAllocated) ?: 0f
                )
            }
        }
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCurrencyForPayment(args.methodId)
        binding.awonarWithdrawBankingButtonSubmit.setOnClickListener {
            binding.awonarWithdrawBankingInputAmount.error = viewModel.validate()
        }
        binding.awonarWithdrawBankingInputAmount.editText?.doOnTextChanged { text, _, _, _ ->
            try {
                val number = text.toString().toFloat()
                viewModel.setAmount(number)
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
        }
    }

}