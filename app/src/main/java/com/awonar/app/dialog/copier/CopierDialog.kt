package com.awonar.app.dialog.copier

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.akexorcist.library.dialoginteractor.DialogLauncher
import com.akexorcist.library.dialoginteractor.InteractorDialog
import com.akexorcist.library.dialoginteractor.createBundle
import com.awonar.app.R
import com.awonar.app.databinding.AwonarDialogCopierBinding
import com.awonar.app.dialog.DialogViewModel
import com.awonar.app.ui.portfolio.PortFolioViewModel
import com.awonar.app.utils.ColorChangingUtil
import com.awonar.app.utils.ImageUtil
import com.molysulfur.library.extension.toast
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
        savedInstanceState: Bundle?,
    ): View {
        launchAndRepeatWithViewLifecycle {
            copyViewModel.stopLossError.collect {
                when (binding.awonarDialogCopierToggleStoploss.checkedButtonId) {
                    R.id.awonar_dialog_copier_toggle_amount -> {
                        binding.awonarDialogCopierNumberpickerStoploss.setHelp(copyViewModel.stopLossError.value.first)
                    }
                    R.id.awonar_dialog_copier_toggle_ratio -> {
                        binding.awonarDialogCopierNumberpickerStoploss.setHelp(copyViewModel.stopLossError.value.second)
                    }
                }
            }
        }
        launchAndRepeatWithViewLifecycle {
            copyViewModel.traderState.collect { user ->
                user?.let {
                    ImageUtil.loadImage(binding.awonarDialogCopierImageAvatar, it.image)
                    binding.username = if (it.displayFullName) "%s %s %s".format(
                        it.firstName,
                        it.middleName,
                        it.lastName) else it.username
                    binding.description = "12 Months return"
                    binding.gain = "%.2f%s".format(it.gain, "%")
                    binding.awonarDialogCopierTextGain.setTextColor(ColorChangingUtil.getTextColorChange(
                        requireContext(),
                        it.gain))
                }
            }
        }
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
                            binding.awonarDialogCopierNumberpickerStoploss.setHelp(copyViewModel.stopLossError.value.first)
                            binding.awonarDialogCopierNumberpickerStoploss.setPrefix("$")
                            binding.awonarDialogCopierNumberpickerStoploss.setNumber(stopLoss.first)
                        }
                        R.id.awonar_dialog_copier_toggle_ratio -> {
                            binding.awonarDialogCopierNumberpickerStoploss.setHelp(copyViewModel.stopLossError.value.second)
                            binding.awonarDialogCopierNumberpickerStoploss.setPrefix("")
                            binding.awonarDialogCopierNumberpickerStoploss.setNumber(stopLoss.second)
                        }
                    }
                }
            }
        }
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val copiesId = arguments?.getString(EXTRA_ID)
        copyViewModel.getTraderInfo(copiesId)
        copyViewModel.getDefaultAmount()
        binding.awonarDialogCopierToolbar.setNavigationOnClickListener {
            dismiss()
        }
        binding.awonarDialogCopierButtonCopy.setOnClickListener {
            copiesId?.let { id ->
                copyViewModel.submitCopy(id)
                toast("Successful.")
                dismiss()
            }
        }
        binding.awonarDialogCopierCheckboxOpenTrade.setOnCheckedChangeListener { buttonView, isChecked ->
            copyViewModel.updateIsCopyExist(isChecked)
        }
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
                    binding.awonarDialogCopierNumberpickerStoploss.setHelp(copyViewModel.stopLossError.value.first)
                    binding.awonarDialogCopierNumberpickerStoploss.setPrefix("$")
                    binding.awonarDialogCopierNumberpickerStoploss.setNumber(copyViewModel.stopLoss.value.first)
                }
                R.id.awonar_dialog_copier_toggle_ratio -> {
                    binding.awonarDialogCopierNumberpickerStoploss.setHelp(copyViewModel.stopLossError.value.second)
                    binding.awonarDialogCopierNumberpickerStoploss.setPrefix("")
                    binding.awonarDialogCopierNumberpickerStoploss.setNumber(copyViewModel.stopLoss.value.second * 100)
                }
            }
        }
        binding.awonarDialogCopierButtonStoplossEdit.setOnClickListener {
            binding.awonarDialogCopierGroupStoploss.visibility = View.VISIBLE
        }
        binding.awonarDialogCopierToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
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
        private var copiesId: String? = null
        private var key: String? = null
        private var data: Bundle? = null


        fun setKey(key: String?): Builder = this.apply {
            this.key = key
        }

        fun setCopiesId(copiesId: String?): Builder = this.apply {
            this.copiesId = copiesId
        }


        fun build(): CopierDialog = CopierDialog.newInstance(key, data, copiesId)
    }

    companion object {

        private const val EXTRA_ID = "com.awonar.app.dialog.copier.extra.id"

        private fun newInstance(
            key: String?,
            data: Bundle?,
            copiesId: String?,
        ) =
            CopierDialog().apply {
                arguments = createBundle(key, data).apply {
                    putString(EXTRA_ID, copiesId)
                }
            }
    }


    override fun bindLauncher(viewModel: DialogViewModel): DialogLauncher<CopierMapper, CopierListener> =
        viewModel.copierDialog

    override fun bindViewModel(): Class<DialogViewModel> = DialogViewModel::class.java
}