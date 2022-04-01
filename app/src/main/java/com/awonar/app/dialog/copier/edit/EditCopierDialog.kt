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
import timber.log.Timber

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
                    copier?.let { copier ->
                        viewModel.validateEditStopLoss(copier)
                    }
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
                when (binding.awonarDialogEditCopierToggleStopcopy.checkedButtonId) {
                    R.id.awonar_dialog_edit_copier_toggle_amount -> {
                        binding.awonarDialogEditCopierNumberpickerStopcopy.setHelp(viewModel.stopLossError.value.first)
                        binding.awonarDialogEditCopierNumberpickerStopcopy.setPrefix("$")
                        binding.awonarDialogEditCopierNumberpickerStopcopy.setNumber(stopLoss.first)
                    }
                    R.id.awonar_dialog_edit_copier_toggle_ratio -> {
                        binding.awonarDialogEditCopierNumberpickerStopcopy.setHelp(viewModel.stopLossError.value.second)
                        binding.awonarDialogEditCopierNumberpickerStopcopy.setPrefix("")
                        binding.awonarDialogEditCopierNumberpickerStopcopy.setNumber(stopLoss.second)
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
            binding.username = if (it.user.isDisplayName) {
                "%s".format(it.user.username)
            } else {
                "%s %s %s".format(it.user.firstName, it.user.middleName, it.user.lastName)
            }
            binding.description = it.parentUsername
            val moneyInOut = copier?.depositSummary?.minus(copier?.withdrawalSummary ?: 0f) ?: 0f
            val netInvest = copier?.initialInvestment?.plus(moneyInOut) ?: 0f
            viewModel.updateAmount(netInvest)
        }
        binding.awonarDialogEditCopierCheckboxOpenTrade.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.updateIsCopyExist(isChecked)
        }
        binding.awonarDialogEditCopierNumberpickerStopcopy.doAfterFocusChange =
            { number, hasFocus ->
                if (!hasFocus) {
                    when (binding.awonarDialogEditCopierToggleStopcopy.checkedButtonId) {
                        R.id.awonar_dialog_edit_copier_toggle_amount -> {
                            viewModel.updateStopLoss(number)
                        }
                        R.id.awonar_dialog_edit_copier_toggle_ratio -> {
                            viewModel.updateRatio(number)
                        }
                    }
                }
            }
        binding.awonarDialogEditCopierToggleStopcopy.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.awonar_dialog_edit_copier_toggle_amount -> {
                        binding.awonarDialogEditCopierNumberpickerStopcopy.setPrefix("$")
                        binding.awonarDialogEditCopierNumberpickerStopcopy.setHelp(viewModel.stopLossError.value.first)
                        binding.awonarDialogEditCopierNumberpickerStopcopy.setNumber(viewModel.stopLoss.value.first)
                    }
                    R.id.awonar_dialog_edit_copier_toggle_ratio -> {
                        binding.awonarDialogEditCopierNumberpickerStopcopy.setPrefix("")
                        binding.awonarDialogEditCopierNumberpickerStopcopy.setHelp(viewModel.stopLossError.value.second)
                        binding.awonarDialogEditCopierNumberpickerStopcopy.setNumber(viewModel.stopLoss.value.second.times(
                            100f))
                    }
                }
            }
        }
        binding.awonarDialogEditCopierButtonCopy.setOnClickListener {
            copier?.let { copier ->
                viewModel.updateCopy(copyId = copier.id)
            }
        }
        binding.awonarDialogEditCopierCheckboxOpenTrade.isChecked =
            copier?.copyExistingPositions == true
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