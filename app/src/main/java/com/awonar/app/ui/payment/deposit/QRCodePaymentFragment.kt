package com.awonar.app.ui.payment.deposit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.awonar.app.databinding.AwonarFragmentPaymentQrcodeBinding
import com.awonar.app.utils.QRCodeUtil
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect

class QRCodePaymentFragment : Fragment() {

    private val viewModel: DepositViewModel by activityViewModels()

    private val args: QRCodePaymentFragmentArgs by navArgs()

    private val binding: AwonarFragmentPaymentQrcodeBinding by lazy {
        AwonarFragmentPaymentQrcodeBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        launchAndRepeatWithViewLifecycle {
            viewModel.qrcodeInfo.collect { info ->
                info?.let {
                    binding.amount = "%.2f %s".format(it.localAmount, it.currencyId)
                    binding.refNo = "%s".format(it.referenceNo)
                    val bitmap = QRCodeUtil.generateQRCode(it.qrCode)
                    binding.awonarPaymentQrcodeImageQr.setImageBitmap(bitmap)
                }
            }
        }
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getDepositQrcode(
            currencyId = args.currencyId,
            methodId = args.methodId,
            redirectUrl = ""
        )

        binding.awonarPaymentQrcodeButtonClose.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.awonarPaymentQrcodeButtonFinished.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}