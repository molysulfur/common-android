package com.awonar.app.ui.order.partialclose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.akexorcist.library.dialoginteractor.DialogLauncher
import com.akexorcist.library.dialoginteractor.InteractorDialog
import com.akexorcist.library.dialoginteractor.createBundle
import com.awonar.android.model.market.Quote
import com.awonar.android.model.portfolio.Position
import com.awonar.android.shared.utils.ConverterQuoteUtil
import com.awonar.app.R
import com.awonar.app.databinding.AwonarDialogPartialcloseBinding
import com.awonar.app.dialog.DialogViewModel
import com.awonar.app.ui.market.MarketViewModel
import com.awonar.app.ui.order.OrderViewModel
import com.awonar.app.utils.ImageUtil
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect

class PartialCloseDialog :
    InteractorDialog<PartialCloseMapper, PartialCloseListener, DialogViewModel>() {

    private lateinit var binding: AwonarDialogPartialcloseBinding
    private val viewModel: OrderViewModel by activityViewModels()
    private val marketViewModel: MarketViewModel by activityViewModels()
    private var position: Position? = null
    private var quote: Quote? = null
    private var isPartial = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AwonarDialogPartialcloseBinding.inflate(inflater)
        launchAndRepeatWithViewLifecycle {
            viewModel.hasPartialState.collect {
                when (it) {
                    true -> binding.awonarPartialCloseCheckboxPart.visibility = View.VISIBLE
                    else -> binding.awonarPartialCloseCheckboxPart.visibility = View.GONE
                }
            }
        }
        launchAndRepeatWithViewLifecycle {
            viewModel.typeChangeState.collect {
                updateNumberPicker(it)
            }
        }
        launchAndRepeatWithViewLifecycle {
            viewModel.amountState.collect {
                updateNumberPicker(it)
                if (position != null && quote != null) {
                    val current = getCurrentPrice()
                    viewModel.validatePartialCloseAmount(position!!, current)
                }
            }
        }
        launchAndRepeatWithViewLifecycle {
            marketViewModel.quoteSteamingState.collect { quotes ->
                if (quote == null) {
                    quote = quotes.find { it.id == position?.instrument?.id }
                    position?.let {
                        viewModel.setDefaultPartialAmount(
                            position = it,
                            price = ConverterQuoteUtil.getCurrentPrice(
                                quote = quote!!,
                                leverage = position?.leverage ?: 1,
                                isBuy = position?.isBuy == true
                            )
                        )
                    }
                }
                quote = quotes.find { it.id == position?.instrument?.id }
                val current = ConverterQuoteUtil.getCurrentPrice(
                    quote = quote!!,
                    leverage = position?.leverage ?: 1,
                    isBuy = position?.isBuy == true
                )
                binding.price = "%s".format(current)
                position?.let {
                    val pl = viewModel.getProfit(current, it)
                    binding.pl = "%.2f".format(pl)
                    binding.total = "%.2f".format(it.amount.plus(pl))
                }
            }
        }
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    private fun getCurrentPrice() = ConverterQuoteUtil.getCurrentPrice(
        quote!!,
        position!!.leverage,
        position!!.isBuy
    )

    private fun updateNumberPicker(it: Pair<Float, Float>) {
        when (binding.awonarPartialCloseButtonGroupType.checkedButtonId) {
            R.id.awonar_partial_close_button_amount -> {
                binding.awonarPartialCloseNumberpickerAmount.setNumber(it.first)
            }
            R.id.awonar_partial_close_button_units -> {
                binding.awonarPartialCloseNumberpickerAmount.setNumber(it.second)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            position = it.getParcelable(EXTRA_POSITION)
        }
        setupInfo()
        setupListener()

    }

    private fun setupInfo() {
        position?.let {
            binding.title = "%s%s".format(if (it.isBuy) "BUY" else "SELL", it.instrument.symbol)
            binding.positionId = "#%s".format(it.positionNo)
            binding.amount = "%.2f".format(it.amount)
            binding.units = "%.2f units".format(it.units)
            ImageUtil.loadImage(binding.awonarPartialCloseImageLogo, it.instrument.logo)
        }
    }

    private fun setupListener() {
        binding.awonarPartialCloseToolbarClosePosition.setNavigationOnClickListener {
            dismiss()
        }
        binding.awonarPartialCloseButtonClosePosition.setOnClickListener {
//            Snackbar.make(requireContext(),binding.root,"Success",Snackbar.LENGTH_SHORT)
//                .setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.awonar_color_green))
//                .show()
            //         dismiss()
//            viewModel.closePosition()
        }
        binding.awonarPartialCloseCheckboxPart.setOnCheckedChangeListener { buttonView, isChecked ->
            isPartial = isChecked
            if (isPartial) {
                binding.awonarPartialCloseGroupInput.visibility = View.VISIBLE
            } else {
                binding.awonarPartialCloseGroupInput.visibility = View.GONE
            }
        }
        binding.awonarPartialCloseButtonGroupType.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                viewModel.toggleAmountType(checkedId)
            }
        }
        binding.awonarPartialCloseNumberpickerAmount.doAfterFocusChange = { number, isFocus ->
            if (!isFocus) {
                position?.let {
                    val current = getCurrentPrice()
                    when (binding.awonarPartialCloseButtonGroupType.checkedButtonId) {
                        R.id.awonar_partial_close_button_amount -> {
                            viewModel.updateAmount(it.instrument.id, number, it.leverage, current)
                        }
                        R.id.awonar_partial_close_button_units -> {
                            viewModel.updateUnits(it.instrument.id, number, it.leverage, current)
                        }
                    }

                }
            }
        }
    }


    override fun bindLauncher(viewModel: DialogViewModel): DialogLauncher<PartialCloseMapper, PartialCloseListener> =
        viewModel.partialClose

    override fun bindViewModel(): Class<DialogViewModel> = DialogViewModel::class.java

    class Builder {
        private var key: String? = null
        private var data: Bundle? = null
        private var position: Position? = null

        fun setPosition(position: Position): Builder = this.apply {
            this.position = position
        }

        fun setKey(key: String?): Builder = this.apply {
            this.key = key
        }

        fun setData(data: Bundle?): Builder = this.apply {
            this.data = data
        }

        fun build(): PartialCloseDialog = newInstance(position, key, data)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NORMAL,
            android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen
        )
    }

    companion object {

        private const val EXTRA_POSITION = "com.awonar.app.ui.order.partial.extra.position"

        private fun newInstance(
            position: Position?,
            key: String?,
            data: Bundle?
        ) =
            PartialCloseDialog().apply {
                arguments = createBundle(key, data).apply {
                    putParcelable(EXTRA_POSITION, position)
                }
            }
    }

}