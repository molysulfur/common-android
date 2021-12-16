package com.awonar.app.ui.payment.deposit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.awonar.android.model.payment.PaymentSetting
import com.awonar.app.R
import com.awonar.app.databinding.AwonarFragmentDepositQrPaymentBinding
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import java.lang.NumberFormatException

class QRPaymentFragment : Fragment() {

    private var paymentInfo: PaymentSetting? = null
    private var defaultName = "THB"
    private var rate = 0f

    private val viewModel: DepositViewModel by activityViewModels()

    private val args: QRPaymentFragmentArgs by navArgs()

    private val binding: AwonarFragmentDepositQrPaymentBinding by lazy {
        AwonarFragmentDepositQrPaymentBinding.inflate(layoutInflater)
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
        launchAndRepeatWithViewLifecycle {
            viewModel.currencyRate.collect {
                rate = it
                setDescriptionCompareRate()
            }
        }
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    private fun setDescriptionCompareRate() {
        binding.awonarDepositQrPaymentInputUsd.helperText =
            "Commission: %.2f | Rate: %s".format(paymentInfo?.commissionDepositDollar ?: 0f, rate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launchAndRepeatWithViewLifecycle {
            viewModel.paymentSetting.collect {
                paymentInfo = it
                setAdapter(it)
            }
        }
        args.methodId.apply {
            viewModel.getCurrencyForPayment(this)
        }
        setupListener()
    }

    private fun setupListener() {
        binding.awonarDepositQrPaymentButtonSubmit.setOnClickListener {
            onSubmit()
        }
        binding.awonarDepositQrPaymentInputAmount.editText?.doOnTextChanged { text, _, _, _ ->
            try {
                val amountCountry = text.toString().toFloat()
                val amountUsd = amountCountry.times(rate)
                viewModel.setAmount(amountCountry)
                binding.awonarDepositQrPaymentInputUsd.editText?.setText("$%.2f".format(amountUsd))
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
        }
        (binding.awonarDepositQrPaymentInputCurrency.editText as? AutoCompleteTextView)?.setOnItemClickListener { _, _, position, _ ->
            defaultName = paymentInfo?.allowDepositCurrencies?.get(position) ?: defaultName
            viewModel.getCurrencyRate(defaultName)
        }
    }

    private fun onSubmit() {
        if ((binding.awonarDepositQrPaymentInputCurrency.editText as? AutoCompleteTextView)?.text.isNullOrBlank()) {
            binding.awonarDepositQrPaymentInputCurrency.error =
                getString(R.string.awonar_text_required)
            return
        }
        binding.awonarDepositQrPaymentInputAmount.error =
            viewModel.validateMinMaxDeposit(defaultName, args.methodId)
    }

    private fun setAdapter(it: PaymentSetting?) {
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.awonar_item_list,
            it?.allowDepositCurrencies ?: emptyList()
        )
        (binding.awonarDepositQrPaymentInputCurrency.editText as? AutoCompleteTextView)?.apply {
            setAdapter(
                adapter
            )
        }
    }
}