package com.awonar.app.dialog.loading

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.akexorcist.library.dialoginteractor.DialogLauncher
import com.akexorcist.library.dialoginteractor.InteractorDialog
import com.akexorcist.library.dialoginteractor.createBundle
import com.awonar.app.databinding.AwonarDialogLoadingBinding
import com.awonar.app.dialog.DialogViewModel
import timber.log.Timber

class LoadingDialog : InteractorDialog<LoadingMapper, LoadingListener, DialogViewModel>() {

    private var title: String? = "Loading..."
    private var subTitle: String? = "Please waiting for response."

    companion object {
        private val EXTRA_TITLE = "com.awonar.app.dialog.loading.extra.title"
        private val EXTRA_SUBTITLE = "com.awonar.app.dialog.loading.extra.subtitle"

        private fun newInstance(
            key: String?,
            data: Bundle?,
            title: String?,
            subTitle: String?,
        ) =
            LoadingDialog().apply {
                arguments = createBundle(key, data).apply {
                    putString(EXTRA_TITLE, title)
                    putString(EXTRA_SUBTITLE, subTitle)
                }
            }
    }

    private lateinit var binding: AwonarDialogLoadingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AwonarDialogLoadingBinding.inflate(inflater, container, false)
        title = arguments?.getString(EXTRA_TITLE, "Loading...")
        subTitle = arguments?.getString(EXTRA_SUBTITLE, "Please waiting for response.")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.e("$title $subTitle")
        binding.title = title
        binding.subTitle = subTitle
    }

    class Builder {
        private var key: String? = null
        private var title: String? = "Loading..."
        private var subTitle: String? = "Please waiting for response."
        private var data: Bundle? = null


        fun setKey(key: String?): Builder = this.apply {
            this.key = key
        }

        fun setTitle(title: String?): Builder = this.apply {
            this.title = title
        }

        fun setSubTitle(subTitle: String?): Builder = this.apply {
            this.subTitle = subTitle
        }


        fun build(): LoadingDialog = newInstance(key, data, title, subTitle)
    }

    override fun bindLauncher(viewModel: DialogViewModel): DialogLauncher<LoadingMapper, LoadingListener> =
        viewModel.loadingDialog

    override fun bindViewModel(): Class<DialogViewModel> = DialogViewModel::class.java
}