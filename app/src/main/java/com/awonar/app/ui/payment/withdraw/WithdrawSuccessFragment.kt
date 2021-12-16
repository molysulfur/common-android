package com.awonar.app.ui.payment.withdraw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.awonar.app.databinding.AwonarFragmentPaymentSuccessBinding

class WithdrawSuccessFragment : Fragment() {


    private val binding: AwonarFragmentPaymentSuccessBinding by lazy {
        AwonarFragmentPaymentSuccessBinding.inflate(layoutInflater)
    }

    private val args: WithdrawSuccessFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.awonarPaymentSuccessVerticalTextId.setTitle(args.withdrawId)
        binding.awonarPaymentSuccessVerticalTextId.setSubTitle("Withdraw ID")
        binding.awonarPaymentSuccessVerticalTextAmount.setTitle("$%.2f".format(args.amount))
        binding.awonarPaymentSuccessVerticalTextAmount.setSubTitle("Withdraw Amount")
        binding.title = "Your withdraw has been processed successfully."
        binding.subTitle = "Hare are the details of transition:."
        binding.awonarPaymentSuccessTextFinished.setOnClickListener {
            findNavController().popBackStack()
        }

    }
}