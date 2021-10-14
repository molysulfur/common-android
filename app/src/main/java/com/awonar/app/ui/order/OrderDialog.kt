package com.awonar.app.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import coil.load
import com.akexorcist.library.dialoginteractor.DialogLauncher
import com.akexorcist.library.dialoginteractor.InteractorDialog
import com.akexorcist.library.dialoginteractor.createBundle
import com.awonar.android.constrant.MarketOrderType
import com.awonar.android.constrant.TPSLType
import com.awonar.android.model.market.Instrument
import com.awonar.android.model.market.Quote
import com.awonar.android.model.order.Price
import com.awonar.android.model.portfolio.Portfolio
import com.awonar.android.shared.constrant.BuildConfig
import com.awonar.android.model.tradingdata.TradingData
import com.awonar.android.shared.utils.ConverterQuoteUtil
import com.awonar.app.R
import com.awonar.app.databinding.AwonarDialogOrderBinding
import com.awonar.app.ui.market.MarketViewModel
import com.awonar.app.ui.market.OrderViewModelActivity
import com.awonar.app.ui.portfolio.PortFolioViewModel
import com.awonar.app.utils.ColorChangingUtil
import com.molysulfur.library.extension.toast
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class OrderDialog : InteractorDialog<OrderMapper, OrderDialogListener, DialogViewModel>() {

    private val marketViewModel: MarketViewModel by activityViewModels()
    private val orderViewModel: OrderViewModel by activityViewModels()
    private val orderActivityViewModel: OrderViewModelActivity by activityViewModels()
    private val portfolioViewModel: PortFolioViewModel by activityViewModels()

    private val binding: AwonarDialogOrderBinding by lazy {
        AwonarDialogOrderBinding.inflate(layoutInflater)
    }

    private lateinit var leverageAdapter: LeverageAdapter
    private var instrument: Instrument? = null
    private var tradingData: TradingData? = null
    private var portfolio: Portfolio? = null
    private var orderType: String? = "buy"
    private lateinit var marketOrderType: MarketOrderType
    private var currentLeverage: Int = 1
    private var amount: Price = Price(0f, 1f, "amount")
    private var quote: Quote? = null
    private var price: Float = 0f
    private var rate: Float = 0f
    private var overnightDaliy: Float = 0f
    private var overnightWeek: Float = 0f

    private var stoploss: Price = Price(amount = 0f, unit = 1f, TPSLType.AMOUNT)
    private var takeProfit: Price = Price(amount = 0f, unit = 1f, TPSLType.AMOUNT)

    companion object {
        private const val EXTRA_INSTRUMENT = "com.awonar.app.ui.dialog.order.extra.instrument"
        private const val EXTRA_ORDER_TYPE = "com.awonar.app.ui.dialog.order.extra.order_type"

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
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        launchAndRepeatWithViewLifecycle {
            launch {
                orderViewModel.openOrderState.collect { success ->
                    dismiss()
                    if (success) {
                        toast("Successfully.")
                    }
                }
            }
            launch {
                orderActivityViewModel.orderTypeState.collect {
                    marketOrderType = it
                }
            }
            launch {
                orderViewModel.overNightFeeWeekState.collect {
                    overnightWeek = it
                    updateOvernight()
                }
            }
            launch {

                orderViewModel.overNightFeeState.collect {
                    overnightDaliy = it
                    updateOvernight()
                }
            }
            launch {
                orderViewModel.stopLossState.collect { sl ->
                    stoploss = sl
                    updateStopLoss()
                }
            }

            launch {
                orderViewModel.takeProfitState.collect { tp ->
                    takeProfit = tp
                    updateTakeProfit()
                }
            }

            launch {
                orderViewModel.exposureState.collect {
                    binding.awonarDialogOrderNumberPickerInputAmount.setNumber(it)
                }
            }

            launch {
                orderViewModel.validateExposureErrorState.collect { message ->
                    message?.let {
                        binding.awonarDialogOrderNumberPickerInputAmount.setHelp(it)
                    }
                }
            }

            launch {
                orderViewModel.getPriceState.collect {
                    amount = it
                    when (amount.type) {
                        "amount" -> binding.awonarDialogOrderNumberPickerInputAmount.setNumber(
                            amount.amount
                        )
                        "unit" -> binding.awonarDialogOrderNumberPickerInputAmount.setNumber(amount.unit)
                    }
                    instrument?.let { instrument ->
                        orderViewModel.getDefaultStopLoss(
                            instrumentId = instrument.id,
                            amount = amount
                        )
                        orderViewModel.getDefaultTakeProfit(
                            instrumentId = instrument.id,
                            amount = amount
                        )
                        getOvernight()
                    }
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
                    updateRate()
                    if (marketOrderType != MarketOrderType.PENDING_ORDER) {
                        updateOrderType()
                    }
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
            }

            launch {
                orderViewModel.maxRateState.collect { rate ->
                    rate?.let {
                        binding.awonarDialogOrderNumberPickerInputRate.setNumber(it)
                    }
                }
            }
        }
        binding.viewModel = orderViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    private fun getOvernight() {
        instrument?.let { instrument ->
            orderViewModel.getOvernightFeeDaliy(
                instrumentId = instrument.id,
                amount = amount,
                leverage = currentLeverage,
                orderType = orderType ?: "buy"
            )
            orderViewModel.getOvernightFeeWeek(
                instrumentId = instrument.id,
                amount = amount,
                leverage = currentLeverage,
                orderType = orderType ?: "buy"
            )
        }

    }

    private fun updateOvernight() {
        Timber.e("$overnightDaliy, $overnightWeek")
        binding.awonarDialogOrderTextOrderOvernight.text =
            "Overnight Fee : Daily: %.2f | Weekend: %.2f".format(overnightDaliy, overnightWeek)
    }

    private fun updateRate() {
        if (marketOrderType != MarketOrderType.PENDING_ORDER) {
            rate = price
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        instrument = arguments?.getParcelable(EXTRA_INSTRUMENT)
        orderType = arguments?.getString(EXTRA_ORDER_TYPE)
        binding.awonarDialogOrderToggleOrderAmountType.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.awonar_dialog_order_button_amount_amount -> {
                        binding.awonarDialogOrderNumberPickerInputAmount.setPrefix("$")
                        amount.type = "amount"
                        calculateAmountOrUnit()
                        updateDetail()
                    }
                    R.id.awonar_dialog_order_button_amount_unit -> {
                        binding.awonarDialogOrderNumberPickerInputAmount.setPrefix("")
                        amount.type = "unit"
                        calculateAmountOrUnit()
                        updateDetail()
                    }
                }
            }
        }
        binding.awonarDialogOrderButtonOpenTrade.setOnClickListener {
            submitOrder()
        }
        initHeaderDialog()
        initLeverageAdapter()
        initListenerInputAmount()
        initStopLoss()
        initTakeProfit()
        initRateListener()
    }

    private fun initRateListener() {
        binding.awonarDialogOrderNumberPickerInputRate.doAfterTextChange = {
            this.rate = it
        }
        binding.awonarDialogOrderNumberPickerInputRate.doAfterFocusChange = { rate, hasFocus ->
            if (!hasFocus) {
                orderViewModel.calculateMinRate(rate, price)
                orderViewModel.calculateMaxRate(rate, price)
            }
        }
        binding.awonarDialogOrderToggleOrderRateType.addOnButtonCheckedListener { _, checkedId, _ ->
            when (checkedId) {
                R.id.awonar_dialog_order_button_rate_amount -> {
                    binding.awonarDialogOrderNumberPickerInputRate.setPlaceHolderEnable(true)
                    updateOrderType()

                }
                R.id.awonar_dialog_order_button_rate_unit -> {
                    binding.awonarDialogOrderNumberPickerInputRate.setPlaceHolderEnable(false)
                    binding.awonarDialogOrderNumberPickerInputRate.setNumber(price)
                    orderActivityViewModel.updateMarketType(MarketOrderType.PENDING_ORDER)
                }
                else -> {
                }
            }
        }
    }

    private fun submitOrder() {
        instrument?.let { instrument ->
            orderViewModel.openOrder(
                instrumentId = instrument.id,
                amount = amount,
                stopLoss = stoploss,
                takeProfit = takeProfit,
                leverage = currentLeverage,
                orderType = orderType ?: "buy",
                rate = rate
            )
        }

    }

    private fun updateOrderType() {
        quote?.let {
            if (it.status == "open") {
                orderActivityViewModel.updateMarketType(MarketOrderType.OPEN_ORDER)
            } else {
                orderActivityViewModel.updateMarketType(MarketOrderType.ENTRY_ORDER)
            }
        }
    }


    private fun initHeaderDialog() {
        instrument?.let {
            binding.awonarDialogOrderImageAvatar.load(BuildConfig.BASE_IMAGE_URL + it.logo)
            binding.awonarDialogOrderTextTitle.text = it.symbol
            binding.awonarDialogOrderTextDescription.text = it.industry
            marketViewModel.getTradingData(it.id)
        }
        binding.awonarDialogOrderImageIconClose.setOnClickListener {
            dismiss()
        }
    }

    private fun initTakeProfit() {
        binding.awonarDialogOrderViewNumberpickerCollapsibleTp.setDescriptionColor(R.color.awonar_color_primary)
        binding.awonarDialogOrderViewNumberpickerCollapsibleTp.onTypeChange = { type ->
            instrument?.let { instrument ->
                orderViewModel.changeTypeTakeProfit(
                    instrumentId = instrument.id,
                    takeProfit = takeProfit,
                    takeProfitType = type,
                    openPrice = price,
                    orderType = orderType,
                    unitOrder = amount.unit
                )
            }
        }
        binding.awonarDialogOrderViewNumberpickerCollapsibleTp.doAfterTextChange = {
            if (instrument != null && quote != null) {
                Timber.e("$takeProfit")
                when (takeProfit.type) {
                    TPSLType.AMOUNT -> {
                        //TODO("")
                    }
                    TPSLType.RATE -> {
                        orderViewModel.validateTakeProfit(
                            takeProfit = takeProfit,
                            openPrice = price,
                            type = orderType ?: "buy"
                        )
                    }
                }
            }
        }
        binding.awonarDialogOrderViewNumberpickerCollapsibleTp.doAfterFocusChange =
            { number, hasFocus ->
                if (!hasFocus) {
                    when (takeProfit.type) {
                        TPSLType.AMOUNT -> {
                            takeProfit.amount = number
                        }
                        TPSLType.RATE -> {
                            takeProfit.unit = number
                        }
                    }
                    updateTakeProfit()
                }
            }
    }

    private fun initStopLoss() {
        binding.awonarDialogOrderViewNumberpickerCollapsibleSl.setDescriptionColor(R.color.awonar_color_orange)
        binding.awonarDialogOrderViewNumberpickerCollapsibleSl.onTypeChange = { type ->
            instrument?.let { instrument ->
                orderViewModel.changeTypeStopLoss(
                    instrumentId = instrument.id,
                    stoploss = stoploss,
                    stopLossType = type,
                    openPrice = price,
                    orderType = orderType,
                    unitOrder = amount.unit
                )
            }
        }
        binding.awonarDialogOrderViewNumberpickerCollapsibleSl.doAfterTextChange = {
            if (instrument != null && quote != null) {
                when (stoploss.type) {
                    TPSLType.AMOUNT -> {
                        orderViewModel.validateStopLoss(
                            instrumentId = instrument!!.id,
                            stoploss = stoploss,
                            leverage = currentLeverage,
                            type = orderType ?: "buy"
                        )
                    }
                    TPSLType.RATE -> {
                        orderViewModel.validateStopLoss(
                            stoploss = stoploss,
                            digit = instrument?.digit ?: 2,
                            openPrice = price,
                            orderType = orderType ?: "buy"
                        )
                    }
                }
            }
        }
        binding.awonarDialogOrderViewNumberpickerCollapsibleSl.doAfterFocusChange =
            { number, hasFocus ->
                if (!hasFocus) {
                    when (stoploss.type) {
                        TPSLType.AMOUNT -> {
                            stoploss.amount = number
                        }
                        TPSLType.RATE -> {
                            stoploss.unit = number
                        }
                    }
                    updateStopLoss()
                }
            }
    }

    private fun updateDetail() {
        if (portfolio?.available ?: 0f < amount.amount) {
            binding.awonarDialogOrderButtonOpenTrade.text = "Deposit"
            binding.awonarDialogOrderTextOrderDetail.text =
                " Deposit %.2f $ for order to open this trade.".format(
                    amount.amount.minus(
                        portfolio?.available ?: 0f
                    )
                )
        } else {
            val amountUnit: Float = when (amount.type) {
                "amount" -> amount.unit
                else -> amount.amount
            }
            val equity: Float = (amount.amount.div(portfolio?.available ?: 0f)) * 100
            binding.awonarDialogOrderTextOrderDetail.text =
                "%.2f Units | %.2f%s of equity | Exposure %.2f".format(
                    amountUnit,
                    equity,
                    "%",
                    amount.amount.times(currentLeverage)
                )
        }
    }

    private fun updateTakeProfit() {
        when (takeProfit.type) {
            TPSLType.AMOUNT -> {
                binding.awonarDialogOrderViewNumberpickerCollapsibleTp.setDescription("${takeProfit.amount}")
                binding.awonarDialogOrderViewNumberpickerCollapsibleTp.setNumber(takeProfit.amount)
                binding.awonarDialogOrderViewNumberpickerCollapsibleTp.setPrefix("$-")
                binding.awonarDialogOrderViewNumberpickerCollapsibleTp.setDigit(0)
            }
            TPSLType.RATE -> {
                binding.awonarDialogOrderViewNumberpickerCollapsibleTp.setDescription("${takeProfit.unit}")
                binding.awonarDialogOrderViewNumberpickerCollapsibleTp.setNumber(takeProfit.unit)
                binding.awonarDialogOrderViewNumberpickerCollapsibleTp.setPrefix("")
                binding.awonarDialogOrderViewNumberpickerCollapsibleTp.setDigit(
                    instrument?.digit ?: 0
                )
            }
        }
    }

    private fun updateStopLoss() {
        when (stoploss.type) {
            TPSLType.AMOUNT -> {
                binding.awonarDialogOrderViewNumberpickerCollapsibleSl.setDescription("${stoploss.amount}")
                binding.awonarDialogOrderViewNumberpickerCollapsibleSl.setPrefix("$-")
                binding.awonarDialogOrderViewNumberpickerCollapsibleSl.setDigit(0)
                binding.awonarDialogOrderViewNumberpickerCollapsibleSl.setNumber(stoploss.amount)

            }
            TPSLType.RATE -> {
                binding.awonarDialogOrderViewNumberpickerCollapsibleSl.setDescription("${stoploss.unit}")
                binding.awonarDialogOrderViewNumberpickerCollapsibleSl.setPrefix("")
                binding.awonarDialogOrderViewNumberpickerCollapsibleSl.setDigit(
                    instrument?.digit ?: 0
                )
                binding.awonarDialogOrderViewNumberpickerCollapsibleSl.setNumber(stoploss.unit)
            }
        }
    }

    private fun initAmount() {
        portfolio?.let {
            amount.amount = it.available.times(0.05f)
            binding.awonarDialogOrderNumberPickerInputAmount.setNumber(amount.amount)
            instrument?.let { instrument ->
                orderViewModel.getUnit(
                    instrumentId = instrument.id,
                    price = price,
                    amount = amount,
                    leverage = currentLeverage
                )
            }
            updateDetail()
            getOvernight()
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

    private fun initLeverageAdapter() {
        leverageAdapter = LeverageAdapter().apply {
            onClick = { text ->
                val newLeverage: Int = text.replace("X", "").toInt()
                currentLeverage = newLeverage
                validateExposure()
                updateDetail()
                getOvernight()
                binding.awonarDialogOrderCollapseLeverage.setDescription("X$newLeverage")
            }
        }
        binding.awonarDialogOrderToggleOrderType.addOnButtonCheckedListener { _, checkedId, _ ->
            when (checkedId) {
                R.id.awonar_dialog_order_button_type_buy -> orderType = "buy"
                R.id.awonar_dialog_order_button_type_sell -> orderType = "sell"
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
                calculateAmountOrUnit()
                validateExposure()
            }
        }
        binding.awonarDialogOrderNumberPickerInputAmount.doAfterTextChange = { number ->
            when (amount.type) {
                "amount" -> {
                    if (amount.amount != number) {
                        validateExposure()
                    }
                    amount.amount = number
                }
                "unit" -> {
                    if (amount.unit != number) {
                        validateExposure()
                    }
                    amount.unit = number
                }
            }

        }
    }

    private fun calculateAmountOrUnit() {
        instrument?.let {
            when (amount.type) {
                "amount" -> orderViewModel.getUnit(
                    instrumentId = it.id,
                    price = price,
                    amount = amount,
                    leverage = currentLeverage
                )
                else -> orderViewModel.getAmount(
                    instrumentId = it.id,
                    price = price,
                    amount = amount,
                    leverage = currentLeverage
                )
            }
        }
    }

    private fun validateExposure() {
        instrument?.let {
            orderViewModel.validatePositionExposure(
                id = it.id,
                amount = amount.amount,
                leverage = currentLeverage,
            )
        }
    }

    private fun updateCurrentPrice() {
        quote?.let {
            price = when (orderType) {
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