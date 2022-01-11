package com.awonar.app.dialog.copier.remove

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.akexorcist.library.dialoginteractor.DialogLauncher
import com.akexorcist.library.dialoginteractor.InteractorDialog
import com.akexorcist.library.dialoginteractor.createBundle
import com.awonar.android.model.portfolio.Copier
import com.awonar.app.databinding.AwonarDialogRemoveCopierBinding
import com.awonar.app.dialog.DialogViewModel
import com.awonar.app.dialog.copier.CopyViewModel
import com.awonar.app.dialog.copier.add.RemoveFundListener
import com.awonar.app.dialog.copier.add.RemoveFundMapper
import com.awonar.app.utils.ImageUtil
import com.molysulfur.library.extension.toast
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class RemoveFundDialog : InteractorDialog<RemoveFundMapper, RemoveFundListener, DialogViewModel>() {

    private val viewModel: CopyViewModel by activityViewModels()

    private var copier: Copier? = null

    private val binding: AwonarDialogRemoveCopierBinding by lazy {
        AwonarDialogRemoveCopierBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        launchAndRepeatWithViewLifecycle {
            launch {
                viewModel.errorState.collect {
                    if (!it.isNullOrBlank()) {
                        dismiss()
                        toast(it)
                    }
                }
            }
            launch {
                viewModel.messageState.collect {
                    if (!it.isNullOrBlank()) {
                        dismiss()
                        toast(it)
                    }
                }
            }
        }
        launchAndRepeatWithViewLifecycle {
            launch {
                viewModel.amountError.collect {
                    binding.awonarDialogRemoveCopierNumberpickerAmount.setHelp(it)
                }
            }
            launch {
                viewModel.amount.collect { amount ->
                    copier?.let {
                        binding.awonarDialogRemoveCopierNumberpickerAmount.setNumber(amount)
                        viewModel.validateRemoveAmount(it)
                    }
                }
            }
        }
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        copier = arguments?.getParcelable<Copier?>(EXTRA_COPIER)
        copier?.let {
            ImageUtil.loadImage(binding.awonarDialogRemoveCopierImageAvatar, it.user.picture)
            binding.username =
                "%s %s %s".format(it.user.fullName, it.user.middleName, it.user.lastName)
            binding.description = it.parentUsername
        }
        binding.awonarDialogRemoveCopierNumberpickerAmount.doAfterFocusChange =
            { value, isFocus ->
                if (!isFocus) {
                    viewModel.updateAmount(value)
                }
            }

        binding.awonarDialogAddCopierButtonAddfund.setOnClickListener {
            copier?.id?.let { id ->
                viewModel.removeFund(id)
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


        fun build(): RemoveFundDialog = newInstance(key, data, copier)
    }

    companion object {

        private const val EXTRA_COPIER = "com.awonar.app.dialog.stopcopier.extra.copier"

        private fun newInstance(
            key: String?,
            data: Bundle?,
            copier: Copier?
        ) =
            RemoveFundDialog().apply {
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

    override fun bindLauncher(viewModel: DialogViewModel): DialogLauncher<RemoveFundMapper, RemoveFundListener> =
        viewModel.addCopierDialog

    override fun bindViewModel(): Class<DialogViewModel> = DialogViewModel::class.java
}