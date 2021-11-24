package com.awonar.app.ui.order.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.akexorcist.library.dialoginteractor.DialogLauncher
import com.akexorcist.library.dialoginteractor.InteractorDialog
import com.akexorcist.library.dialoginteractor.createBundle
import com.awonar.android.model.portfolio.Position
import com.awonar.android.shared.utils.ConverterQuoteUtil
import com.awonar.android.shared.utils.PortfolioUtil
import com.awonar.app.R
import com.awonar.app.databinding.AwonarDialogOrderEditBinding
import com.awonar.app.dialog.DialogViewModel
import com.awonar.app.ui.market.MarketViewModel
import com.awonar.app.ui.order.OrderViewModel
import com.awonar.app.utils.ColorChangingUtil
import com.awonar.app.utils.DateUtils
import com.awonar.app.utils.ImageUtil
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import timber.log.Timber

class OrderEditDialog : InteractorDialog<OrderEditMapper, OrderEditListener, DialogViewModel>() {

    private lateinit var binding: AwonarDialogOrderEditBinding

    private var position: Position? = null
    private var pl: Float = 0f
    private var price: Float = 0f
    private val marketViewModel: MarketViewModel by activityViewModels()
    private val orderViewModel: OrderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AwonarDialogOrderEditBinding.inflate(inflater)
        binding.awonarOrderEditTextNumberpickerTp.setPrefix("$")
        binding.awonarOrderEditTextNumberpickerSl.setPrefix("$")
        binding.awonarOrderEditTextNumberpickerTp.setDescriptionColor(R.color.awonar_color_primary)
        binding.awonarOrderEditTextNumberpickerSl.setDescriptionColor(R.color.awonar_color_orange)
        launchAndRepeatWithViewLifecycle {
            orderViewModel.takeProfitError.collect {
                binding.awonarOrderEditTextNumberpickerTp.setHelp(it)
            }
        }
        launchAndRepeatWithViewLifecycle {
            orderViewModel.stopLossError.collect {
                binding.awonarOrderEditTextNumberpickerSl.setHelp(it)
            }
        }
        launchAndRepeatWithViewLifecycle {
            orderViewModel.stopLossState.collect {
                position?.let {
                    orderViewModel.validateStopLoss(
                        position = it,
                        current = price
                    )
                }
                binding.awonarOrderEditTextNumberpickerSl.setNumber(it)
            }
        }
        launchAndRepeatWithViewLifecycle {
            orderViewModel.takeProfitState.collect {
                position?.let {
                    orderViewModel.validateTakeProfit(
                        position = it,
                        current = price
                    )
                }
                binding.awonarOrderEditTextNumberpickerTp.setNumber(it)
            }
        }
        launchAndRepeatWithViewLifecycle {
            marketViewModel.quoteSteamingState.collect { quotes ->
                val quote = quotes.find { it.id == position?.instrument?.id }
                quote?.let {
                    price = ConverterQuoteUtil.getCurrentPrice(
                        it,
                        position?.leverage ?: 0,
                        position?.isBuy == true
                    )
                    pl = PortfolioUtil.getProfitOrLoss(
                        current = price,
                        openRate = position?.openRate ?: 0f,
                        unit = position?.units ?: 0f,
                        rate = 1f,
                        isBuy = position?.isBuy == true
                    )
                    val priceChange = ConverterQuoteUtil.change(
                        quote.close,
                        quote.previous
                    )
                    val pricePercentChange = ConverterQuoteUtil.percentChange(
                        quote.previous,
                        quote.close,
                    )
                    binding.pl = "$%.2f".format(pl)
                    binding.plPercent = "%.2f%s".format(
                        PortfolioUtil.profitLossPercent(pl, position?.amount ?: 0f),
                        "%"
                    )
                    binding.awonarOrderEditTextPl.setTextColor(
                        ColorChangingUtil.getTextColorChange(
                            requireContext(),
                            pl
                        )
                    )
                    binding.awonarOrderEditTextPlPercent.setTextColor(
                        ColorChangingUtil.getTextColorChange(
                            requireContext(),
                            pl
                        )
                    )
                    binding.price = "%s".format(price)
                    binding.priceChange = "%.2f (%.2f)".format(priceChange, pricePercentChange)
                    binding.status = it.status
                    binding.description = position?.instrument?.categories?.get(0) ?: ""
                    binding.awonarOrderEditTextChange.setTextColor(
                        ColorChangingUtil.getTextColorChange(
                            priceChange
                        )
                    )
                }
            }
        }
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.run {
            position = getParcelable(EXTRA_POSITION)
        }
        setupHeader()
        setupListener()
        setupTakeProfit()
        setupStopLoss()
    }

    private fun setupStopLoss() {
        orderViewModel.setStopLoss(
            sl = position?.stopLossRate ?: 0f,
            type = "rate",
            current = position?.openRate ?: 0f,
            unit = position?.units ?: 0f,
            instrumentId = position?.instrument?.id ?: 1,
            isBuy = position?.isBuy == true
        )
    }

    private fun setupTakeProfit() {
        orderViewModel.setTakeProfit(
            tp = position?.takeProfitRate ?: 0f,
            type = "rate",
            current = position?.openRate ?: 0f,
            unit = position?.units ?: 0f,
            instrumentId = position?.instrument?.id ?: 1,
            isBuy = position?.isBuy == true
        )
    }

    private fun setupListener() {
        binding.awonarOrderEditTextNumberpickerTp.doAfterToggle = { isLeft ->
            if (isLeft) {
                binding.awonarOrderEditTextNumberpickerTp.setDescription(
                    "$%.2f".format(
                        orderViewModel.takeProfitState.value.first
                    )
                )
            } else {
                binding.awonarOrderEditTextNumberpickerTp.setDescription(
                    "%s".format(
                        orderViewModel.takeProfitState.value.second
                    )
                )
            }
        }
        binding.awonarOrderEditTextNumberpickerSl.doAfterToggle = { isLeft ->
            if (isLeft) {
                binding.awonarOrderEditTextNumberpickerSl.setDescription(
                    "$%.2f".format(
                        orderViewModel.stopLossState.value.first
                    )
                )
            } else {
                binding.awonarOrderEditTextNumberpickerSl.setDescription(
                    "%s".format(
                        orderViewModel.stopLossState.value.second
                    )
                )
            }
        }
        binding.awonarOrderEditTextNumberpickerTp.doAfterFocusChange = { number, isLeft ->
            val type = if (isLeft) {
                "amount"
            } else {
                "rate"
            }
            orderViewModel.setTakeProfit(
                tp = if (isLeft) number.first else number.second,
                type = type,
                current = position?.openRate ?: 0f,
                unit = position?.units ?: 0f,
                instrumentId = position?.instrument?.id ?: 1,
                isBuy = position?.isBuy == true
            )
        }
        binding.awonarOrderEditTextNumberpickerSl.doAfterFocusChange = { number, isLeft ->
            val type = if (isLeft) {
                "amount"
            } else {
                "rate"
            }
            orderViewModel.setStopLoss(
                sl = if (isLeft) number.first else number.second,
                type = type,
                current = position?.openRate ?: 0f,
                unit = position?.units ?: 0f,
                instrumentId = position?.instrument?.id ?: 1,
                isBuy = position?.isBuy == true
            )
        }
    }

    private fun setupHeader() {
        ImageUtil.loadImage(binding.awonarOrderEditImageAvatar, position?.instrument?.logo)
        binding.awonarOrderEditTextTitle.text = "%s %s".format(
            if (position?.isBuy == true) "BUY" else "SELL",
            position?.instrument?.symbol
        )
        binding.amount = "$%.2f".format(position?.amount)
        binding.unit = "%.3f Unit".format(position?.units)
        binding.rate = "%s".format(position?.openRate)
        binding.date = "%s".format(DateUtils.getDate(position?.openDateTime))
        binding.fee = "$%.2f".format(position?.totalFees)
        binding.leverage = "X%s".format(position?.leverage)
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

        fun build(): OrderEditDialog = newInstance(position, key, data)
    }

    companion object {

        private const val EXTRA_POSITION = "com.awonar.app.ui.order.edit.extra.position"

        private fun newInstance(
            position: Position?,
            key: String?,
            data: Bundle?
        ) =
            OrderEditDialog().apply {
                arguments = createBundle(key, data).apply {
                    putParcelable(EXTRA_POSITION, position)
                }
            }
    }

    override fun bindLauncher(viewModel: DialogViewModel): DialogLauncher<OrderEditMapper, OrderEditListener> =
        viewModel.orderEdit

    override fun bindViewModel(): Class<DialogViewModel> = DialogViewModel::class.java
}