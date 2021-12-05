package com.awonar.app.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.akexorcist.library.dialoginteractor.DialogLauncher
import com.akexorcist.library.dialoginteractor.InteractorDialog
import com.akexorcist.library.dialoginteractor.createBundle
import com.awonar.android.constrant.MarketOrderType
import com.awonar.android.constrant.TPSLType
import com.awonar.android.model.market.Instrument
import com.awonar.android.model.market.Quote
import com.awonar.android.model.order.Price
import com.awonar.android.model.portfolio.Portfolio
import com.awonar.android.model.tradingdata.TradingData
import com.awonar.android.shared.utils.ConverterQuoteUtil
import com.awonar.app.R
import com.awonar.app.databinding.AwonarDialogOrderBinding
import com.awonar.app.dialog.DialogViewModel
import com.awonar.app.ui.market.MarketViewModel
import com.awonar.app.ui.portfolio.PortFolioViewModel
import com.awonar.app.utils.ColorChangingUtil
import com.awonar.app.utils.loadImage
import com.molysulfur.library.extension.toast
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class OrderDialog : InteractorDialog<OrderMapper, OrderDialogListener, DialogViewModel>() {

    private val binding: AwonarDialogOrderBinding by lazy {
        AwonarDialogOrderBinding.inflate(layoutInflater)
    }

    private val marketViewModel: MarketViewModel by activityViewModels()
    private val orderViewModel: OrderViewModel by activityViewModels()
    private val orderActivityViewModel: OrderViewModelActivity by activityViewModels()
    private val portfolioViewModel: PortFolioViewModel by activityViewModels()

    private var instrument: Instrument? = null
    private var tradingData: TradingData? = null
    private var portfolio: Portfolio? = null

    private var marketType: MarketOrderType = MarketOrderType.ENTRY_ORDER
    private var isBuy: Boolean? = true
    private var quote: Quote? = null
    private var price: Float = 0f
    private var currentLeverage: Int = 1
    private var takeProfit: Price = Price(amount = 0f, unit = 1f, TPSLType.AMOUNT)

    private lateinit var leverageAdapter: LeverageAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        launchAndRepeatWithViewLifecycle {
            launch {
                orderActivityViewModel.getOrderRequest.collect {
                    orderViewModel.openOrder(it)
                }
            }
            launch {
                orderActivityViewModel.takeProfit.collect { tp ->

                }
            }
            launch {
                orderActivityViewModel.stopLossState.collect {
                    if (it != null)
                        validateStoploss()
                }
            }
            launch {
                orderViewModel.amountState.collect {
//                    binding.awonarDialogOrderNumberPickerInputAmount.setNumber(it)
                    validateExposure()
//                    getDefaultTpAndSl()
//                    updateDetail()
//                    getOvernight()
                }
            }
            launch {
                orderActivityViewModel.leverageState.collect {
                    currentLeverage = it
                    binding.awonarDialogOrderNumberPickerInputAmount.setHelp("")
                    binding.awonarDialogOrderCollapseLeverage.setDescription("X$it")
                    updateDetail()
                    getOvernight()
                }
            }
            launch {
                orderActivityViewModel.rateState.collect {
                    instrument?.run {
                        orderActivityViewModel.calculateMaxRate(it, price, digit)
                        orderActivityViewModel.calculateMinRate(it, price, digit)
                    }
                }
            }
            launch {
                orderViewModel.openOrderState.collect { message ->
                    dismiss()
                    toast(message)
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
                    orderViewModel.updateRate(price, marketType)
                    updateCurrentPrice()
                }
            }
            launch {
                marketViewModel.tradingDataState.collect {
                    if (it != null) {
                        tradingData = it
                        tradingData?.let { tradingData ->
                            updateLeverage(tradingData.defaultLeverage)
                            initValueWithTradingData()
                        }
                    }
                }
            }
            launch {
                orderViewModel.exposureState.collect {
                    instrument?.let { instrument ->
                        orderActivityViewModel.updateAmount(instrument.id, price, it)
                    }
                }
            }
            launch {
                orderViewModel.exposureError.collect {
                    binding.awonarDialogOrderNumberPickerInputAmount.setHelp(it)
                }
            }
        }
        binding.viewModel = orderViewModel
        binding.activityViewModel = orderActivityViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        instrument = arguments?.getParcelable(EXTRA_INSTRUMENT)
        isBuy = arguments?.getBoolean(EXTRA_TYPE, true)
        binding.digit = instrument?.digit ?: 0
        binding.awonarDialogOrderToggleOrderAmountType.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.awonar_dialog_order_button_amount_amount -> {
                        orderActivityViewModel.updateAmountType("amount")
                        updateDetail()
                    }
                    R.id.awonar_dialog_order_button_amount_unit -> {
                        orderActivityViewModel.updateAmountType("unit")
                        updateDetail()
                    }
                }
            }
        }
        binding.awonarDialogOrderButtonOpenTrade.setOnClickListener {
            instrument?.let { instrument ->
//                orderActivityViewModel.getOrderRequest(
//                    instrumentId = instrument.id,
//                    orderType = isBuy
//                )
            }
        }
        setupHeader()
        setupRate()
        initLeverageAdapter()
        initListenerInputAmount()
        initStopLoss()
        initTakeProfit()
    }

    private fun setupRate() {
        instrument?.run {
            binding.awonarDialogOrderNumberPickerInputRate.setDigit(digit)
            binding.awonarDialogOrderNumberPickerInputRate.setPrefix("")
            binding.awonarDialogOrderNumberPickerInputRate.doAfterTextChange = {
                orderViewModel.updateRate(it, marketType)
            }
        }

        binding.awonarDialogOrderToggleOrderRateType.addOnButtonCheckedListener { _, checkedId, _ ->
            when (checkedId) {
                R.id.awonar_dialog_order_button_rate_market -> {
                    marketType = if (quote?.status == "open")
                        MarketOrderType.OPEN_ORDER
                    else
                        MarketOrderType.ENTRY_ORDER
                }
                R.id.awonar_dialog_order_button_rate_manual -> {
                    marketType = MarketOrderType.PENDING_ORDER
                }
                else -> {
                }
            }
        }
    }

    private fun setupHeader() {
        instrument?.let {
            binding.awonarDialogOrderImageAvatar.loadImage(it.logo)
            binding.awonarDialogOrderTextTitle.text = it.symbol
            binding.awonarDialogOrderTextDescription.text = it.industry
            marketViewModel.getTradingData(it.id)
        }
        binding.awonarDialogOrderImageIconClose.setOnClickListener {
            dismiss()
        }
    }

    private fun getDefaultTpAndSl() {
        instrument?.let { instrument ->
//            orderActivityViewModel.getDefaultStopLoss(
//                instrumentId = instrument.id,
//                price = price,
//                orderType = isBuy
//            )
//            orderActivityViewModel.getDefaultTakeProfit(
//                instrumentId = instrument.id,
//                price = price,
//                orderType = isBuy
//            )
        }
    }

    private fun validateStoploss() {
        instrument?.let { instrument ->
//            orderActivityViewModel.validateStopLoss(
//                instrument,
//                price,
//                orderType ?: "buy"
//            )
        }
    }

    private fun updateLeverage(leverage: Int) {
        instrument?.let {
            orderActivityViewModel.updateLeverage(
                it.id,
                price,
                leverage
            )
        }
    }

    private fun getOvernight() {
        instrument?.let { instrument ->
//            orderActivityViewModel.getOvernightFeeDaliy(
//                instrumentId = instrument.id,
//                orderType = isBuy
//            )
//            orderActivityViewModel.getOvernightFeeWeek(
//                instrumentId = instrument.id,
//                orderType = isBuy
//            )
        }

    }

    private fun initTakeProfit() {
        binding.awonarDialogOrderViewNumberpickerCollapsibleTp.setDescriptionColor(R.color.awonar_color_primary)
//        binding.awonarDialogOrderViewNumberpickerCollapsibleTp.onTypeChange = { type ->
//            instrument?.let { instrument ->
//                orderActivityViewModel.updateTakeProfitType(type)
//            }
//        }
        binding.awonarDialogOrderViewNumberpickerCollapsibleTp.doAfterTextChange = {
            if (instrument != null && quote != null) {
                when (takeProfit.type) {
                    TPSLType.AMOUNT -> {
                        //TODO("")
                    }
                    TPSLType.RATE -> {
//                        orderViewModel.validateTakeProfit(
//                            takeProfit = takeProfit,
//                            openPrice = price,
//                            type = orderType ?: "buy"
//                        )
                    }
                }
            }
        }
        binding.awonarDialogOrderViewNumberpickerCollapsibleTp.doAfterFocusChange =
            { number, hasFocus ->
                if (!hasFocus) {

                }
            }
    }

    private fun initStopLoss() {
        binding.awonarDialogOrderViewNumberpickerCollapsibleSl.setDescriptionColor(R.color.awonar_color_orange)
//        binding.awonarDialogOrderViewNumberpickerCollapsibleSl.onTypeChange = { type ->
//            orderActivityViewModel.updateStopLossType(type)
//        }
        binding.awonarDialogOrderViewNumberpickerCollapsibleSl.doAfterFocusChange =
            { number, hasFocus ->
                if (!hasFocus)
                    instrument?.let {
//                        orderActivityViewModel.updateStopLoss(
//                            (-number),
//                            orderType ?: "buy",
//                            it.id,
//                            price
//                        )
                    }

            }
    }

    private fun updateDetail() {
        orderActivityViewModel.getDetail(portfolio?.available ?: 0f)
    }

    private fun initAmount() {
        portfolio?.let { portfolio ->
            instrument?.let { instrument ->
                orderViewModel.setDefaultAmount(
                    instrumentId = instrument.id,
                    available = portfolio.available,
                    price = price,
                )
            }

        }
    }

    private fun initValueWithTradingData() {
        if (tradingData?.allowSell == false) {
            binding.awonarDialogOrderButtonTypeSell.isEnabled = tradingData?.allowSell == false
            isBuy = if (isBuy == false) true else isBuy
        }

        if (tradingData?.allowBuy == false) {
            binding.awonarDialogOrderButtonTypeBuy.isEnabled = tradingData?.allowBuy == false
            isBuy = if (isBuy == true) null else isBuy
        }
        when (isBuy) {
            true -> binding.awonarDialogOrderToggleOrderType.check(R.id.awonar_dialog_order_button_type_buy)
            false -> binding.awonarDialogOrderToggleOrderType.check(R.id.awonar_dialog_order_button_type_sell)
            else -> {
            }
        }

        leverageAdapter.leverageString = tradingData?.leverages ?: emptyList()
        binding.awonarDialogOrderCollapseLeverage.setDescription("X$currentLeverage")
    }

    private fun initLeverageAdapter() {
        leverageAdapter = LeverageAdapter().apply {
            onClick = { text ->
                val newLeverage: Int = text.replace("X", "").toInt()
                updateLeverage(newLeverage)
                validateExposure()
            }
        }
        binding.awonarDialogOrderToggleOrderType.addOnButtonCheckedListener { _, checkedId, _ ->
            when (checkedId) {
                R.id.awonar_dialog_order_button_type_buy -> isBuy = true
                R.id.awonar_dialog_order_button_type_sell -> isBuy = false
            }
            updateCurrentPrice()
            getOvernight()
        }
        binding.awonarDialogOrderCollapseLeverage.setTitle(getString(R.string.awonar_text_leverage))
        binding.awonarDialogOrderCollapseLeverage.setAdapter(leverageAdapter)
    }

    private fun initListenerInputAmount() {
        binding.awonarDialogOrderNumberPickerInputAmount.doAfterFocusChange = { number, hasFocus ->
            if (!hasFocus) {
                instrument?.run {
//                    orderViewModel.updateAmount(id, price, number)
                }
            }
        }
    }

    private fun validateExposure() {
        instrument?.let {
            orderActivityViewModel.validatePositionExposure(
                id = it.id
            )
        }
    }

    private fun updateCurrentPrice() {
        quote?.let {
            price = ConverterQuoteUtil.getCurrentPrice(it, currentLeverage, isBuy == true)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NORMAL,
            android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen
        )
    }

    class Builder {
        private var instrument: Instrument? = null
        private var isBuy: Boolean = true
        private var key: String? = null
        private var data: Bundle? = null

        fun setSymbol(instrument: Instrument?): Builder = this.apply {
            this.instrument = instrument
        }

        fun setType(isBuy: Boolean): Builder = this.apply {
            this.isBuy = isBuy
        }

        fun setKey(key: String?): Builder = this.apply {
            this.key = key
        }

        fun setData(data: Bundle?): Builder = this.apply {
            this.data = data
        }

        fun build(): OrderDialog = newInstance(instrument, isBuy, key, data)
    }

    override fun bindLauncher(viewModel: DialogViewModel): DialogLauncher<OrderMapper, OrderDialogListener> =
        viewModel.order

    override fun bindViewModel(): Class<DialogViewModel> = DialogViewModel::class.java

    companion object {
        private const val EXTRA_INSTRUMENT = "com.awonar.app.ui.dialog.order.extra.instrument"
        private const val EXTRA_TYPE = "com.awonar.app.ui.dialog.order.extra.order_type"

        private fun newInstance(
            instrument: Instrument?,
            isBuy: Boolean,
            key: String?,
            data: Bundle?
        ) =
            OrderDialog().apply {
                arguments = createBundle(key, data).apply {
                    putParcelable(EXTRA_INSTRUMENT, instrument)
                    putBoolean(EXTRA_TYPE, isBuy)
                }
            }
    }

}