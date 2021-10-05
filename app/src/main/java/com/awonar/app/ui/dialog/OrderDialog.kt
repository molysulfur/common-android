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
import com.awonar.android.model.market.Quote
import com.awonar.android.model.portfolio.Portfolio
import com.awonar.android.shared.constrant.BuildConfig
import com.awonar.android.model.tradingdata.TradingData
import com.awonar.android.shared.utils.ConverterQuoteUtil
import com.awonar.app.R
import com.awonar.app.databinding.AwonarDialogOrderBinding
import com.awonar.app.ui.market.MarketViewModel
import com.awonar.app.ui.portfolio.PortFolioViewModel
import com.awonar.app.utils.ColorChangingUtil
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class OrderDialog : InteractorDialog<OrderMapper, OrderDialogListener, DialogViewModel>() {


    private val marketViewModel: MarketViewModel by activityViewModels()
    private val orderViewModel: OrderViewModel by activityViewModels()
    private val portfolioViewModel: PortFolioViewModel by activityViewModels()

    private val binding: AwonarDialogOrderBinding by lazy {
        AwonarDialogOrderBinding.inflate(layoutInflater)
    }

    private lateinit var leverageAdapter: LeverageAdapter
    private var instrument: Instrument? = null
    private var tradingData: TradingData? = null
    private var portfolio: Portfolio? = null
    private var orderType: String? = "buy"
    private var currentLeverage: Int = 1
    private var amountType: String = "amount"
    private var amount: Float = 0f
    private var quote: Quote? = null

    companion object {
        const val EXTRA_INSTRUMENT = "com.awonar.app.ui.dialog.order.extra.instrument"
        const val EXTRA_ORDER_TYPE = "com.awonar.app.ui.dialog.order.extra.order_type"

        private fun newInstance(
            instrument: Instrument?,
            orderType: String,
            key: String?,
            data: Bundle?
        ) =
            OrderDialog().apply {
                arguments = createBundle(key, data).apply {
                    putParcelable(EXTRA_INSTRUMENT, instrument)
                    putString(EXTRA_ORDER_TYPE, orderType)
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
            launch {
                orderViewModel.validateExposureErrorState.collect { message ->
                    message?.let {
                        binding.awonarDialogOrderNumberPickerInputAmount.setHelp(it)
                    }

                }
            }
            launch {
                orderViewModel.getAmountState.collect {
                    binding.awonarDialogOrderNumberPickerInputAmount.setNumber(it)
                }
            }
            launch {
                portfolioViewModel.portfolioState.collect {
                    if (it != null) {
                        portfolio = it
                        initAmount()
                    }
                }
            }
            launch {
                marketViewModel.quoteSteamingState.collect { quotes ->
                    quote = quotes.find { it.id == instrument?.id }
                    updateCurrentPrice()
                }
            }

            launch {
                marketViewModel.tradingDataState.collect {
                    if (it != null) {
                        tradingData = it
                        currentLeverage = tradingData?.defaultLeverage ?: 1
                        initValueWithTradingData()
                    }
                }
            }

            launch {
                orderViewModel.minRateState.collect { rate ->
                    rate?.let {
                        binding.awonarDialogOrderNumberPickerInputRate.setNumber(it)
                    }
                }
                orderViewModel.maxRateState.collect { rate ->
                    rate?.let {
                        binding.awonarDialogOrderNumberPickerInputRate.setNumber(it)
                    }
                }
            }

        }
        return binding.root
    }

    private fun initAmount() {
        portfolio?.let {
            amount = it.available.times(0.05f)
            binding.awonarDialogOrderNumberPickerInputAmount.setNumber(amount)
        }
    }

    private fun initValueWithTradingData() {
        if (tradingData?.allowSell == false) {
            binding.awonarDialogOrderButtonTypeSell.isEnabled = tradingData?.allowSell == false
            orderType = if (orderType == "sell") "buy" else orderType
        }

        if (tradingData?.allowBuy == false) {
            binding.awonarDialogOrderButtonTypeBuy.isEnabled = tradingData?.allowBuy == false
            orderType = if (orderType == "buy") null else orderType
        }
        when (orderType) {
            "buy" -> binding.awonarDialogOrderToggleOrderType.check(R.id.awonar_dialog_order_button_type_buy)
            "sell" -> binding.awonarDialogOrderToggleOrderType.check(R.id.awonar_dialog_order_button_type_sell)
            else -> {
            }
        }

        leverageAdapter.leverageString = tradingData?.leverages ?: emptyList()
        binding.awonarDialogOrderCollapseLeverage.setDescription("X$currentLeverage")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        instrument = arguments?.getParcelable(EXTRA_INSTRUMENT)
        orderType = arguments?.getString(EXTRA_ORDER_TYPE)
        instrument?.let {
            binding.awonarDialogOrderImageAvatar.load(BuildConfig.BASE_IMAGE_URL + it.logo)
            binding.awonarDialogOrderTextTitle.text = it.symbol
            binding.awonarDialogOrderTextDescription.text = it.industry
            marketViewModel.getTradingData(it.id)
        }
        binding.awonarDialogOrderImageIconClose.setOnClickListener {
            dismiss()
        }
        binding.awonarDialogOrderNumberPickerInputRate.doAfterFocusChange = { rate, hasFocus ->
            if (!hasFocus) {
                quote?.let {
                    val price: Float = when (orderType) {
                        "buy" -> {
                            if (currentLeverage > 1) it.ask else it.askSpread
                        }
                        else -> {
                            it.bidSpread
                        }
                    }
                    orderViewModel.calculateMinRate(rate, price)
                    orderViewModel.calculateMaxRate(rate, price)
                }
            }
        }
        binding.awonarDialogOrderToggleOrderRateType.addOnButtonCheckedListener { _, checkedId, _ ->
            when (checkedId) {
                R.id.awonar_dialog_order_button_rate_amount -> {
                    binding.awonarDialogOrderNumberPickerInputRate.setPlaceHolderEnable(
                        true
                    )
                }
                R.id.awonar_dialog_order_button_rate_unit -> {
                    binding.awonarDialogOrderNumberPickerInputRate.setPlaceHolderEnable(false)
                    quote?.let {
                        val price: Float = when (orderType) {
                            "buy" -> {
                                if (currentLeverage > 1) it.ask else it.askSpread
                            }
                            else -> {
                                it.bidSpread
                            }
                        }
                        binding.awonarDialogOrderNumberPickerInputRate.setNumber(price)
                    }
                }
                else -> {
                }
            }
        }
        binding.awonarDialogOrderNumberPickerInputAmount.doAfterFocusChange = { number, hasFocus ->
            if (!hasFocus) {
                instrument?.let {
                    validateExposure()
                }
            }
        }
        binding.awonarDialogOrderNumberPickerInputAmount.doAfterTextChange = { number ->
            amount = number
        }
        binding.awonarDialogOrderToggleOrderAmountType.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.awonar_dialog_order_button_amount_amount -> {
                        binding.awonarDialogOrderNumberPickerInputAmount.setPrefix("$")
                        amountType = "amount"
                        updateAmountInput()
                    }
                    R.id.awonar_dialog_order_button_amount_unit -> {
                        binding.awonarDialogOrderNumberPickerInputAmount.setPrefix("")
                        amountType = "unit"
                        updateAmountInput()
                    }
                }
            }
        }
        leverageAdapter = LeverageAdapter().apply {
            onClick = { text ->
                val newLeverage: Int = text.replace("X", "").toInt()
                currentLeverage = newLeverage
                validateExposure()
                binding.awonarDialogOrderCollapseLeverage.setDescription("X$newLeverage")
            }
        }
        binding.awonarDialogOrderToggleOrderType.addOnButtonCheckedListener { group, checkedId, isChecked ->
            when (checkedId) {
                R.id.awonar_dialog_order_button_type_buy -> orderType = "buy"
                R.id.awonar_dialog_order_button_type_sell -> orderType = "sell"
            }
            updateCurrentPrice()
        }
        binding.awonarDialogOrderCollapseLeverage.setTitle(getString(R.string.awonar_text_leverage))
        binding.awonarDialogOrderCollapseLeverage.setAdapter(leverageAdapter)
    }

    private fun updateAmountInput() {
        quote?.let { quote ->
            instrument?.let {
                val price: Float = when (orderType) {
                    "buy" -> {
                        if (currentLeverage > 1) quote.ask else quote.askSpread
                    }
                    else -> {
                        quote.bidSpread
                    }
                }
                orderViewModel.getAmountOrUnit(
                    instrumentId = it.id,
                    price = price,
                    amount = amount,
                    leverage = currentLeverage,
                    type = amountType
                )
            }
        }
    }

    private fun validateExposure() {
        quote?.let { quote ->
            instrument?.let {
                orderViewModel.validatePositionExposure(
                    id = it.id,
                    number = amount,
                    leverage = currentLeverage,
                    type = amountType,
                    price = when (orderType) {
                        "buy" -> {
                            if (currentLeverage > 1) quote.ask else quote.askSpread
                        }
                        else -> {
                            quote.bidSpread
                        }
                    }
                )
            }
        }
    }

    private fun updateCurrentPrice() {
        quote?.let {
            val price: Float = when (orderType) {
                "buy" -> {
                    if (currentLeverage > 1) it.ask else it.askSpread
                }
                else -> {
                    it.bidSpread
                }
            }
            binding.awonarDialogOrderTextPrice.text = "$price"
            val change = ConverterQuoteUtil.change(price, it.previous)
            val percentChange = ConverterQuoteUtil.percentChange(price, it.previous)
            binding.awonarDialogOrderTextChange.text =
                "$%.${instrument?.digit ?: 2}f (%.2f%s)".format(change, percentChange, "%")
            binding.awonarDialogOrderTextChange.setTextColor(
                ColorChangingUtil.getTextColorChange(
                    requireContext(),
                    change
                )
            )
            binding.awonarDialogOrderTextMarketStatus.text = it.status
        }
    }


    class Builder {
        private var instrument: Instrument? = null
        private var orderType: String = "buy"
        private var key: String? = null
        private var data: Bundle? = null

        fun setSymbol(instrument: Instrument?): Builder = this.apply {
            this.instrument = instrument
        }

        fun setType(orderType: String): Builder = this.apply {
            this.orderType = orderType
        }

        fun setKey(key: String?): Builder = this.apply {
            this.key = key
        }

        fun setData(data: Bundle?): Builder = this.apply {
            this.data = data
        }

        fun build(): OrderDialog = newInstance(instrument, orderType, key, data)
    }

    override fun bindLauncher(viewModel: DialogViewModel): DialogLauncher<OrderMapper, OrderDialogListener> =
        viewModel.order

    override fun bindViewModel(): Class<DialogViewModel> = DialogViewModel::class.java
}