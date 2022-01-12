package com.awonar.app.dialog.copier.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.akexorcist.library.dialoginteractor.DialogLauncher
import com.akexorcist.library.dialoginteractor.InteractorDialog
import com.akexorcist.library.dialoginteractor.createBundle
import com.awonar.android.model.portfolio.Copier
import com.awonar.app.databinding.AwonarDialogAddCopierBinding
import com.awonar.app.dialog.DialogViewModel
import com.awonar.app.dialog.copier.CopyViewModel
import com.awonar.app.dialog.copier.remove.RemoveFundDialog
import com.awonar.app.utils.ImageUtil
import com.molysulfur.library.extension.toast
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class AddFundDialog : InteractorDialog<AddFundMapper, AddFundListener, DialogViewModel>() {

    private val viewModel: CopyViewModel by activityViewModels()

    private val binding: AwonarDialogAddCopierBinding by lazy {
        AwonarDialogAddCopierBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
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
                    binding.awonarDialogAddCopierNumberpickerAmount.setHelp(it)
                }
            }
            launch {
                viewModel.amount.collect {
                    binding.awonarDialogAddCopierNumberpickerAmount.setNumber(it)
                    viewModel.validateAmount()
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
            ImageUtil.loadImage(binding.awonarDialogAddCopierImageAvatar, it.user.picture)
            binding.username =
                "%s %s %s".format(it.user.fullName, it.user.middleName, it.user.lastName)
            binding.description = it.parentUsername
        }
        binding.awonarDialogAddCopierToolbar.setNavigationOnClickListener {
            dismiss()
        }
        binding.awonarDialogAddCopierNumberpickerAmount.doAfterFocusChange =
            { value, isFocus ->
                if (!isFocus) {
                    viewModel.updateAmount(value)
                }
            }

        binding.awonarDialogAddCopierButtonAddfund.setOnClickListener {
            copier?.id?.let { id ->
                viewModel.addFund(id)
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


        fun build(): AddFundDialog = newInstance(key, data, copier)
    }

    companion object {

        private const val EXTRA_COPIER = "com.awonar.app.dialog.addfund.extra.copier"

        private fun newInstance(
            key: String?,
            data: Bundle?,
            copier: Copier?,
        ) =
            AddFundDialog().apply {
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


    override fun bindViewModel(): Class<DialogViewModel> = DialogViewModel::class.java
    override fun bindLauncher(viewModel: DialogViewModel): DialogLauncher<AddFundMapper, AddFundListener> =
        viewModel.addCopierDialog
}