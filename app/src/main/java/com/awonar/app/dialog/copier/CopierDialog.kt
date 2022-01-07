package com.awonar.app.dialog.copier

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.akexorcist.library.dialoginteractor.DialogLauncher
import com.akexorcist.library.dialoginteractor.InteractorDialog
import com.akexorcist.library.dialoginteractor.createBundle
import com.awonar.app.R
import com.awonar.app.databinding.AwonarDialogCopierBinding
import com.awonar.app.dialog.DialogViewModel
import com.awonar.app.ui.portfolio.PortFolioViewModel
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CopierDialog : InteractorDialog<CopierMapper, CopierListener, DialogViewModel>() {

    private val binding: AwonarDialogCopierBinding by lazy {
        AwonarDialogCopierBinding.inflate(layoutInflater)
    }

    private val copyViewModel: CopyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        launchAndRepeatWithViewLifecycle {
            launch {
                copyViewModel.amount.collect { amount ->
                    binding.awonarDialogCopierNumberpickerAmount.setNumber(amount)
                    copyViewModel.updateStopLoss(amount.times(0.4f))
                }
            }
            launch {
                copyViewModel.amountError.collect {
                    binding.awonarDialogCopierNumberpickerAmount.setHelp(it)
                }
            }
        }
        launchAndRepeatWithViewLifecycle {
            launch {
                copyViewModel.stopLoss.collect { stopLoss ->
                    binding.stopLossDescription =
                        "Stop copying if copy value drops below : $%.2f".format(stopLoss.first)
                    when (binding.awonarDialogCopierToggleStoploss.checkedButtonId) {
                        R.id.awonar_dialog_copier_toggle_amount -> {
                            binding.awonarDialogCopierNumberpickerStoploss.setPrefix("$")
                            binding.awonarDialogCopierNumberpickerStoploss.setNumber(stopLoss.first)
                        }
                        R.id.awonar_dialog_copier_toggle_ratio -> {
                            binding.awonarDialogCopierNumberpickerStoploss.setPrefix("")
                            binding.awonarDialogCopierNumberpickerStoploss.setNumber(stopLoss.second)
                        }
                    }
                }
            }
            launch {
                copyViewModel.stopLossError.collect {
                    binding.awonarDialogCopierNumberpickerStoploss.setHelp(it)
                }
            }
        }
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.awonarDialogCopierNumberpickerAmount.doAfterFocusChange = { number, hasFocus ->
            if (!hasFocus) {
                copyViewModel.updateAmount(number)
            }
        }
        binding.awonarDialogCopierNumberpickerAmount.doAfterTextChange = { number ->
            copyViewModel.validateAmount()
        }

        binding.awonarDialogCopierNumberpickerStoploss.doAfterTextChange = {
            copyViewModel.validateStopLoss()
        }
        binding.awonarDialogCopierNumberpickerStoploss.doAfterFocusChange = { number, hasFocus ->
            if (!hasFocus) {
                when (binding.awonarDialogCopierToggleStoploss.checkedButtonId) {
                    R.id.awonar_dialog_copier_toggle_amount -> {
                        copyViewModel.updateStopLoss(number)
                    }
                    R.id.awonar_dialog_copier_toggle_ratio -> {
                        copyViewModel.updateRatio(number)
                    }
                }
            }
        }
        binding.awonarDialogCopierToggleStoploss.addOnButtonCheckedListener { group, checkedId, isChecked ->
            when (checkedId) {
                R.id.awonar_dialog_copier_toggle_amount -> {
                    binding.awonarDialogCopierNumberpickerStoploss.setPrefix("$")
                    binding.awonarDialogCopierNumberpickerStoploss.setNumber(copyViewModel.stopLoss.value.first)
                }
                R.id.awonar_dialog_copier_toggle_ratio -> {
                    binding.awonarDialogCopierNumberpickerStoploss.setPrefix("")
                    binding.awonarDialogCopierNumberpickerStoploss.setNumber(copyViewModel.stopLoss.value.second * 100)
                }
            }
        }
        binding.awonarDialogCopierButtonStoplossEdit.setOnClickListener {
            binding.awonarDialogCopierGroupStoploss.visibility = View.VISIBLE
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NORMAL,
            android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen
        )
    }

    class Builder {
        private var key: String? = null
        private var data: Bundle? = null


        fun setKey(key: String?): Builder = this.apply {
            this.key = key
        }


        fun build(): CopierDialog = CopierDialog.newInstance(key, data)
    }

    companion object {

        private fun newInstance(
            key: String?,
            data: Bundle?
        ) =
            CopierDialog().apply {
                arguments = createBundle(key, data).apply {
                }
            }
    }


    override fun bindLauncher(viewModel: DialogViewModel): DialogLauncher<CopierMapper, CopierListener> =
        viewModel.copierDialog

    override fun bindViewModel(): Class<DialogViewModel> = DialogViewModel::class.java
}