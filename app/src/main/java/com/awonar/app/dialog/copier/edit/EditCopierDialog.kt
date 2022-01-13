package com.awonar.app.dialog.copier.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.akexorcist.library.dialoginteractor.DialogLauncher
import com.akexorcist.library.dialoginteractor.InteractorDialog
import com.akexorcist.library.dialoginteractor.createBundle
import com.awonar.android.model.portfolio.Copier
import com.awonar.app.R
import com.awonar.app.databinding.AwonarDialogEditCopierBinding
import com.awonar.app.dialog.DialogViewModel
import com.awonar.app.dialog.copier.CopyViewModel
import com.awonar.app.utils.ImageUtil
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class EditCopierDialog :
    InteractorDialog<EditCopierMapper, EditCopierListener, DialogViewModel>() {

    private val viewModel: CopyViewModel by activityViewModels()

    private val binding: AwonarDialogEditCopierBinding by lazy {
        AwonarDialogEditCopierBinding.inflate(layoutInflater)
    }

    private var copier: Copier? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        launchAndRepeatWithViewLifecycle {
            launch {
                viewModel.stopLoss.collect {
                    viewModel.validateStopLoss()
                }
            }
            launch {
                viewModel.amount.collect {
                    viewModel.updateStopLoss(copier?.stopLossPercentage ?: 0f)
                }
            }
        }
        launchAndRepeatWithViewLifecycle {
            viewModel.stopLoss.collect { stopLoss ->
                binding.stopLossDescription =
                    "Stop copying if copy value drops below : $%.2f".format(stopLoss.first)
                when (binding.awonarDialogEditCopierToggleStoploss.checkedButtonId) {
                    R.id.awonar_dialog_edit_copier_toggle_amount -> {
                        binding.awonarDialogEditCopierNumberpickerStoploss.setHelp(viewModel.stopLossError.value.first)
                        binding.awonarDialogEditCopierNumberpickerStoploss.setPrefix("$")
                        binding.awonarDialogEditCopierNumberpickerStoploss.setNumber(stopLoss.first)
                    }
                    R.id.awonar_dialog_edit_copier_toggle_ratio -> {
                        binding.awonarDialogEditCopierNumberpickerStoploss.setHelp(viewModel.stopLossError.value.second)
                        binding.awonarDialogEditCopierNumberpickerStoploss.setPrefix("")
                        binding.awonarDialogEditCopierNumberpickerStoploss.setNumber(stopLoss.second)
                    }
                }
            }
        }
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        copier = arguments?.getParcelable(EXTRA_COPIER)
        copier?.let {
            ImageUtil.loadImage(binding.awonarDialogEditCopierImageAvatar, it.user.picture)
            binding.username =
                "%s %s %s".format(it.user.fullName, it.user.middleName, it.user.lastName)
            binding.description = it.parentUsername
            val moneyInOut = copier?.depositSummary?.minus(copier?.withdrawalSummary ?: 0f) ?: 0f
            val netInvest = copier?.initialInvestment?.plus(moneyInOut) ?: 0f
            viewModel.updateAmount(netInvest)
        }
        binding.awonarDialogEditCopierNumberpickerStoploss.doAfterFocusChange =
            { number, hasFocus ->
                if (!hasFocus) {
                    when (binding.awonarDialogEditCopierToggleStoploss.checkedButtonId) {
                        R.id.awonar_dialog_edit_copier_toggle_amount -> {
                            viewModel.updateStopLoss(number)
                        }
                        R.id.awonar_dialog_edit_copier_toggle_ratio -> {
                            viewModel.updateRatio(number)
                        }
                    }
                }
            }
        binding.awonarDialogEditCopierToggleStoploss.addOnButtonCheckedListener { group, checkedId, isChecked ->
            when (binding.awonarDialogEditCopierToggleStoploss.checkedButtonId) {
                R.id.awonar_dialog_edit_copier_toggle_amount -> {
                    binding.awonarDialogEditCopierNumberpickerStoploss.setHelp(viewModel.stopLossError.value.first)
                    binding.awonarDialogEditCopierNumberpickerStoploss.setPrefix("$")
                    binding.awonarDialogEditCopierNumberpickerStoploss.setNumber(viewModel.stopLoss.value.first)
                }
                R.id.awonar_dialog_edit_copier_toggle_ratio -> {
                    binding.awonarDialogEditCopierNumberpickerStoploss.setHelp(viewModel.stopLossError.value.second)
                    binding.awonarDialogEditCopierNumberpickerStoploss.setPrefix("")
                    binding.awonarDialogEditCopierNumberpickerStoploss.setNumber(viewModel.stopLoss.value.second.times(
                        100f))
                }
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

        fun build(): EditCopierDialog = newInstance(key, data, copier)
    }

    companion object {

        private const val EXTRA_COPIER = "com.awonar.app.dialog.edit.extra.copier"

        private fun newInstance(
            key: String?,
            data: Bundle?,
            copier: Copier?,
        ) =
            EditCopierDialog().apply {
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


    override fun bindLauncher(viewModel: DialogViewModel): DialogLauncher<EditCopierMapper, EditCopierListener> =
        viewModel.editStopLossDialog

    override fun bindViewModel(): Class<DialogViewModel> = DialogViewModel::class.java
}