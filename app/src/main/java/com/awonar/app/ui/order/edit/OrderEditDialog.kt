package com.awonar.app.ui.order.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.akexorcist.library.dialoginteractor.DialogLauncher
import com.akexorcist.library.dialoginteractor.InteractorDialog
import com.akexorcist.library.dialoginteractor.createBundle
import com.awonar.android.model.market.Instrument
import com.awonar.android.model.portfolio.Position
import com.awonar.android.shared.utils.ConverterQuoteUtil
import com.awonar.android.shared.utils.PortfolioUtil
import com.awonar.app.databinding.AwonarDialogOrderEditBinding
import com.awonar.app.dialog.DialogViewModel
import com.awonar.app.ui.market.MarketViewModel
import com.awonar.app.ui.order.OrderDialog
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
    private var price: Float = 0f
    private val marketViewModel: MarketViewModel by activityViewModels()
    private val orderViewModel: OrderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AwonarDialogOrderEditBinding.inflate(inflater)
        launchAndRepeatWithViewLifecycle {
            orderViewModel.takeProfitState.collect {
                binding.awonarOrderEditTextNumberpickerTp.setNumber(it.first)
                Timber.e("${it.first} ${it.second}")
            }
        }
        launchAndRepeatWithViewLifecycle {
            marketViewModel.quoteSteamingState.collect { quotes ->
                val quote = quotes.find { it.id == position?.instrument?.id }
                quote?.let {
                    price = if (position?.isBuy == true) it.bid else it.ask
                    val priceChange = ConverterQuoteUtil.change(
                        quote.close,
                        quote.previous
                    )
                    val pricePercentChange = ConverterQuoteUtil.percentChange(
                        quote.previous,
                        quote.close,
                    )
                    val pl = PortfolioUtil.getProfitOrLoss(
                        current = price,
                        openRate = position?.openRate ?: 0f,
                        unit = position?.units ?: 0f,
                        rate = 1f,
                        isBuy = position?.isBuy == true
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
        orderViewModel.setTakeProfit(
            tp = position?.takeProfitRate ?: 0f,
            type = "rate",
            current = price,
            unit = position?.units ?: 0f,
            instrumentId = position?.instrument?.id ?: 1,
            isBuy = position?.isBuy == true
        )
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