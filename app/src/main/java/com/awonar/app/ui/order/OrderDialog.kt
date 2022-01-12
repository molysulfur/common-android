package com.awonar.app.ui.order

import android.annotation.SuppressLint
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
import com.awonar.android.shared.steaming.QuoteSteamingManager
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
import timber.log.Timber

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
        savedInstanceState: Bundle?,
    ): View {
        /**
         * Amount Obsever
         */
        launchAndRepeatWithViewLifecycle {
            launch {
                orderActivityViewModel.exposureError.collect { error ->
                    error?.let {
                        binding.awonarDialogOrderNumberPickerInputAmount.setNumber(it.value)
                        binding.awonarDialogOrderNumberPickerInputAmount.setHelp(it.message ?: "")
                    }
                }
            }
            launch {
                orderViewModel.amountState.collect { amount ->
                    when (binding.awonarDialogOrderToggleOrderAmountType.checkedButtonId) {
                        R.id.awonar_dialog_order_button_amount_amount -> {
                            binding.awonarDialogOrderNumberPickerInputAmount.setNumber(
                                amount.first)
                        }
                        R.id.awonar_dialog_order_button_amount_unit -> {
                            binding.awonarDialogOrderNumberPickerInputAmount.setNumber(
                                amount.second)
                        }
                    }
                    validateExposure(amount = amount.first)
//                    getDefaultTpAndSl()
//                    updateDetail()
//                    getOvernight()
                }
            }
        }
        /**
         * Rate Obsever
         */
        launchAndRepeatWithViewLifecycle {
            launch {
                orderActivityViewModel.rateErrorState.collect {
                    it?.let {
                        binding.awonarDialogOrderNumberPickerInputRate.setNumber(it.rate)
                        binding.awonarDialogOrderNumberPickerInputRate.setHelp(it.message ?: "")
                    }
                }
            }
            launch {
                orderViewModel.openRate.collect { rate ->
                    rate?.let {
                        orderActivityViewModel.validateRate(it, price, instrument?.digit ?: 0)
                    }
                }
            }
        }
        /**
         * streaming data
         */
        launchAndRepeatWithViewLifecycle {
            QuoteSteamingManager.quotesState.collect { quotes ->
                quote = quotes[instrument?.id]
                orderViewModel.updateRate(price, marketType)
                updateCurrentPrice()
            }
        }
        /**
         * stop loss Observer
         */
        launchAndRepeatWithViewLifecycle {
            orderViewModel.stopLossState.collect {

            }
        }
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
                orderViewModel.leverageState.collect {
                    currentLeverage = it
                    binding.awonarDialogOrderNumberPickerInputAmount.setHelp("")
                    binding.awonarDialogOrderCollapseLeverage.setDescription("X$it")
                    updateDetail()
                    getOvernight()
                }
            }
            launch {
                orderViewModel.openOrderState.collect { message ->
                    dismiss()
                    toast(message)
                }
            }

        }
        binding.viewModel = orderViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        instrument = arguments?.getParcelable(EXTRA_INSTRUMENT)
        isBuy = arguments?.getBoolean(EXTRA_TYPE, true)
        launchAndRepeatWithViewLifecycle {
            marketViewModel.tradingDataState.collect {
                if (it != null) {
                    tradingData = it
                    tradingData?.let { tradingData ->
                        instrument?.let { instrument ->
                            orderViewModel.updateLeverage(
                                leverage = tradingData.defaultLeverage,
                                instrumentId = instrument.id,
                                price = price
                            )
                        }

                        initValueWithTradingData()
                    }
                }
            }
        }
        launchAndRepeatWithViewLifecycle {
            portfolioViewModel.portfolioState.collect {
                if (it != null) {
                    portfolio = it
                    setupDefaultAmount()
                }
            }
        }
        binding.digit = instrument?.digit ?: 0
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
        setupAmount()
        setupLeverageAdapter()
        initStopLoss()
        initTakeProfit()
    }

    private fun setupAmount() {
        binding.awonarDialogOrderNumberPickerInputAmount.doAfterFocusChange = { number, hasFocus ->
            if (!hasFocus) {
                when (binding.awonarDialogOrderToggleOrderAmountType.checkedButtonId) {
                    R.id.awonar_dialog_order_button_amount_amount -> {
                        instrument?.id?.let {
                            orderViewModel.updateAmount(
                                instrumentId = it,
                                amount = number,
                                price = price
                            )
                        }

                    }
                    R.id.awonar_dialog_order_button_amount_unit -> {
                        instrument?.id?.let {
                            orderViewModel.updateUnits(
                                instrumentId = it,
                                units = number,
                                price = price
                            )
                        }
                    }
                }
            }
        }
        binding.awonarDialogOrderToggleOrderAmountType.addOnButtonCheckedListener { _, checkedId, _ ->
            when (checkedId) {
                R.id.awonar_dialog_order_button_amount_amount -> {
                    binding.awonarDialogOrderNumberPickerInputAmount.setNumber(orderViewModel.amountState.value.first)
                    binding.awonarDialogOrderNumberPickerInputAmount.setPrefix("$")
                }
                R.id.awonar_dialog_order_button_amount_unit -> {
                    binding.awonarDialogOrderNumberPickerInputAmount.setNumber(orderViewModel.amountState.value.second)
                    binding.awonarDialogOrderNumberPickerInputAmount.setPrefix("")
                }
            }
        }
    }

    private fun setupRate() {
        instrument?.run {
            binding.awonarDialogOrderNumberPickerInputRate.setDigit(digit)
            binding.awonarDialogOrderNumberPickerInputRate.setPrefix("")
            binding.awonarDialogOrderNumberPickerInputRate.doAfterTextChange = {
                orderViewModel.updateRate(it)
            }
        }
        binding.awonarDialogOrderToggleOrderRateType.addOnButtonCheckedListener { _, checkedId, _ ->
            when (checkedId) {
                R.id.awonar_dialog_order_button_rate_market -> {
                    binding.awonarDialogOrderNumberPickerInputRate.setPlaceHolderEnable(true)
                    marketType = if (quote?.status == "open")
                        MarketOrderType.OPEN_ORDER
                    else
                        MarketOrderType.ENTRY_ORDER
                }
                R.id.awonar_dialog_order_button_rate_manual -> {
                    binding.awonarDialogOrderNumberPickerInputRate.setNumber(price)
                    binding.awonarDialogOrderNumberPickerInputRate.setPlaceHolderEnable(false)
                    marketType = MarketOrderType.PENDING_ORDER
                }
                else -> {
                }
            }
        }
    }

    private fun setupHeader() {
        instrument?.let { instrument ->
            binding.awonarDialogOrderImageAvatar.loadImage(instrument.logo)
            binding.awonarDialogOrderTextTitle.text = instrument.symbol
            binding.awonarDialogOrderTextDescription.text = instrument.industry
            marketViewModel.getTradingData(instrument.id)
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

    private fun setupDefaultAmount() {
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

    private fun setupLeverageAdapter() {
        leverageAdapter = LeverageAdapter().apply {
            onClick = { text ->
                val newLeverage: Int = text.replace("X", "").toInt()
                instrument?.id?.let { id ->
                    orderViewModel.updateLeverage(newLeverage, id, price)
                }
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

    private fun validateExposure(amount: Float) {
        instrument?.let { instrument ->
            orderActivityViewModel.validatePositionExposure(
                id = instrument.id,
                amount = amount,
                leverage = currentLeverage
            )
        }
    }

    @SuppressLint("SetTextI18n")
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
            data: Bundle?,
        ) =
            OrderDialog().apply {
                arguments = createBundle(key, data).apply {
                    putParcelable(EXTRA_INSTRUMENT, instrument)
                    putBoolean(EXTRA_TYPE, isBuy)
                }
            }
    }

}