package com.awonar.app.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import coil.load
import com.akexorcist.library.dialoginteractor.DialogLauncher
import com.akexorcist.library.dialoginteractor.InteractorDialog
import com.akexorcist.library.dialoginteractor.createBundle
import com.awonar.android.model.market.Instrument
import com.awonar.android.shared.constrant.BuildConfig
import com.awonar.app.databinding.AwonarDialogOrderBinding
import com.awonar.app.ui.market.MarketViewModel
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect

class OrderDialog : InteractorDialog<OrderMapper, OrderDialogListener, DialogViewModel>() {

    private var instrument: Instrument? = null

    private val marketViewModel: MarketViewModel by activityViewModels()

    private val binding: AwonarDialogOrderBinding by lazy {
        AwonarDialogOrderBinding.inflate(layoutInflater)
    }


    companion object {
        const val EXTRA_INSTRUMENT = "com.awonar.app.ui.dialog.order.extra.instrument"

        private fun newInstance(instrument: Instrument?, key: String?, data: Bundle?) =
            OrderDialog().apply {
                arguments = createBundle(key, data).apply {
                    putParcelable(EXTRA_INSTRUMENT, instrument)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NORMAL,
            android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen
        );
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        launchAndRepeatWithViewLifecycle {
            marketViewModel.quoteSteamingState.collect { quotes ->
                val quote = quotes.find { it.id == instrument?.id }
                binding.awonarDialogOrderTextPrice.text = "${quote?.bid ?: 0f}"
                binding.awonarDialogOrderTextMarketStatus.text = quote?.status
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        instrument = arguments?.getParcelable(EXTRA_INSTRUMENT)
        instrument?.let {
            binding.awonarDialogOrderImageAvatar.load(BuildConfig.BASE_IMAGE_URL + it.logo)
            binding.awonarDialogOrderTextTitle.text = it.symbol
            binding.awonarDialogOrderTextDescription.text = it.industry
        }
        binding.awonarDialogOrderImageIconClose.setOnClickListener {
            dismiss()
        }
    }

    class Builder {
        private var instrument: Instrument? = null
        private var key: String? = null
        private var data: Bundle? = null

        fun setSymbol(instrument: Instrument?): Builder = this.apply {
            this.instrument = instrument
        }

        fun setKey(key: String?): Builder = this.apply {
            this.key = key
        }

        fun setData(data: Bundle?): Builder = this.apply {
            this.data = data
        }

        fun build(): OrderDialog = newInstance(instrument, key, data)
    }

    override fun bindLauncher(viewModel: DialogViewModel): DialogLauncher<OrderMapper, OrderDialogListener> =
        viewModel.order

    override fun bindViewModel(): Class<DialogViewModel> = DialogViewModel::class.java
}