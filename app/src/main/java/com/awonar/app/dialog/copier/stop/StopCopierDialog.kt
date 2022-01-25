package com.awonar.app.dialog.copier.stop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.akexorcist.library.dialoginteractor.DialogLauncher
import com.akexorcist.library.dialoginteractor.InteractorDialog
import com.akexorcist.library.dialoginteractor.createBundle
import com.awonar.android.model.portfolio.Copier
import com.awonar.app.databinding.AwonarDialogStopCopierBinding
import com.awonar.app.dialog.DialogViewModel
import com.awonar.app.dialog.copier.CopyViewModel
import com.awonar.app.utils.ColorChangingUtil
import com.awonar.app.utils.ImageUtil
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class StopCopierDialog : InteractorDialog<StopCopierMapper, StopCopierListener, DialogViewModel>() {

    private val binding: AwonarDialogStopCopierBinding by lazy {
        AwonarDialogStopCopierBinding.inflate(layoutInflater)
    }

    private val viewModel: CopyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        launchAndRepeatWithViewLifecycle {
            launch {
                viewModel.messageState.collect {
                    if (!it.isNullOrBlank()) {
                        getListener().onSuccess(it)
                        dismiss()
                    }
                }
            }
            launch {
                viewModel.errorState.collect {
                    binding.error = it
                }
            }
        }
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val copier = arguments?.getParcelable<Copier?>(EXTRA_COPIER)
        copier?.let {
            val netInvest =
                copier.initialInvestment.plus(copier.depositSummary).minus(copier.withdrawalSummary)
            val profit = it.closedPositionsNetProfit
            val fee = copier.totalFees
            ImageUtil.loadImage(binding.awonarDialogStopCopierImageAvatar, it.user.picture)
            binding.netInvest = "$%.2f".format(netInvest)
            binding.profit = "$%.2f".format(profit)
            binding.fee = "$%.2f".format(fee)
            binding.total = "$%.2f".format(netInvest.plus(profit).plus(fee))
            binding.awonarDialogStopCopierTextProfitloss.setTextColor(
                ColorChangingUtil.getTextColorChange(
                    requireContext(),
                    profit
                )
            )
        }
        binding.awonarDialogStopCopierToolbar.setNavigationOnClickListener {
            dismiss()
        }
        binding.awonarDialogStopCopierButtonStop.setOnClickListener {
            copier?.let { copy ->
                viewModel.stopCopy(copy.id)
            }
        }
    }


    class Builder {
        private var copier: Copier? = null
        private var key: String? = null
        private var data: Bundle? = null


        fun setKey(key: String?): Builder = this.apply {
            this.key = key
        }

        fun setCopies(copier: Copier?): Builder = this.apply {
            this.copier = copier
        }


        fun build(): StopCopierDialog = newInstance(key, data, copier)
    }

    companion object {

        private const val EXTRA_COPIER = "com.awonar.app.dialog.stopcopier.extra.copier"

        private fun newInstance(
            key: String?,
            data: Bundle?,
            copier: Copier?
        ) =
            StopCopierDialog().apply {
                arguments = createBundle(key, data).apply {
                    putParcelable(EXTRA_COPIER, copier)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NORMAL,
            android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen
        )
    }

    override fun bindLauncher(viewModel: DialogViewModel): DialogLauncher<StopCopierMapper, StopCopierListener> =
        viewModel.stopCopierDialog

    override fun bindViewModel(): Class<DialogViewModel> = DialogViewModel::class.java
}