package com.awonar.app.ui.payment.withdraw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.awonar.app.R
import com.awonar.app.databinding.AwonarFragmentWithdrawOtpBinding
import com.awonar.app.ui.user.UserViewModel
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import java.lang.NumberFormatException

class WithdrawOTPFragment : Fragment() {

    private val viewModel: WithdrawViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()

    private val binding: AwonarFragmentWithdrawOtpBinding by lazy {
        AwonarFragmentWithdrawOtpBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        launchAndRepeatWithViewLifecycle{
            viewModel.navigationActions.collect {
                findNavController().navigate(it)
            }
        }
        launchAndRepeatWithViewLifecycle {
            viewModel.amount.collect {
                binding.awonarWithdrawOtpVerticalTextAmount.setTitle("$%.2f".format(it))
            }
        }
        binding.userViewModel = userViewModel
        binding.withdrawViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.awonarWithdrawOtpButtonSubmit.setOnClickListener {
            try {
                viewModel.onSubmit(
                    binding.awonarWithdrawOtpInputOtp.editText?.text.toString().toInt(),
                    userViewModel.bankState.value?.id ?: ""
                )
            } catch (e: NumberFormatException) {
                e.printStackTrace()
                binding.awonarWithdrawOtpInputOtp.error = getString(R.string.awonar_text_required)
            }
        }
    }
}