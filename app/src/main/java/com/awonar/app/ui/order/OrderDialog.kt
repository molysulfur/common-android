package com.awonar.app.ui.order

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.akexorcist.library.dialoginteractor.DialogLauncher
import com.akexorcist.library.dialoginteractor.InteractorDialog
import com.akexorcist.library.dialoginteractor.createBundle
import com.awonar.android.model.market.Instrument
import com.awonar.android.shared.steaming.QuoteSteamingManager
import com.awonar.app.R
import com.awonar.app.databinding.AwonarDialogOrderBinding
import com.awonar.app.dialog.DialogViewModel
import com.awonar.app.ui.order.leverageselector.adpater.LeverageSelectorAdapter
import com.awonar.app.ui.portfolio.PortFolioViewModel
import com.google.android.material.textfield.TextInputEditText
import com.molysulfur.library.extension.toast
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch
import timber.log.Timber

class OrderDialog : InteractorDialog<OrderMapper, OrderDialogListener, DialogViewModel>() {

    private val binding: AwonarDialogOrderBinding by lazy {
        AwonarDialogOrderBinding.inflate(layoutInflater)
    }

    private val orderViewModel: OrderViewModel by activityViewModels()
    private val orderActivityViewModel: OrderViewModelActivity by activityViewModels()
    private val portfolioViewModel: PortFolioViewModel by activityViewModels()
    private val leverageAdapter: LeverageSelectorAdapter = LeverageSelectorAdapter().apply {
        onChecked = {
            orderViewModel.updateLeverage(it)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        observeActivityViewModel()
        observeError()
        launchAndRepeatWithViewLifecycle {
//            orderViewModel.depositState.collectLatest { isDeposit ->
//                if (isDeposit) {
//                    binding.awonarDialogOrderButtonOpenTrade.text = "Deposit"
//                } else {
//                    binding.awonarDialogOrderButtonOpenTrade.text = "Open Trade"
//                }
//            }
        }
        launchAndRepeatWithViewLifecycle {
//            orderViewModel.overnightFeeMessage.collectLatest {
//                binding.awonarDialogOrderTextOrderOvernight.text = it
//            }
        }
        /**
         * Current Price and Changed
         */
        launchAndRepeatWithViewLifecycle {
            orderViewModel.priceState.collect {
                binding.awonarDialogOrderTextPrice.text =
                    "%.${orderViewModel.instrument.value?.digit}f".format(it)
            }
        }
        launchAndRepeatWithViewLifecycle {
            orderViewModel.changeState.collect {
                binding.awonarDialogOrderTextChange.text =
                    "%.${orderViewModel.instrument.value?.digit}f (%.2f%s)".format(
                        it.first,
                        it.second, "%"
                    )
            }
        }
        /**
         * streaming data
         */
        launchAndRepeatWithViewLifecycle {
            QuoteSteamingManager.quotesState.collect { quotes ->
                orderViewModel.updatePrice(quotes)
            }
        }
        launchAndRepeatWithViewLifecycle {
            orderViewModel.bidState.collect { bid ->
                binding.awonarDialogOrderButtonTypeBuy.text = "BUY $bid"
            }
        }
        launchAndRepeatWithViewLifecycle {
            orderViewModel.askState.collect { ask ->
                binding.awonarDialogOrderButtonTypeSell.text = "Sell $ask"
            }
        }
        /**
         * instrument observe
         */
        launchAndRepeatWithViewLifecycle {
            orderViewModel.instrument.collect { instrument ->
                instrument?.let {
                    /**
                     * init input rate
                     */
//                    binding.awonarDialogOrderNumberPickerInputRate.setDigit(it.digit)
//                    binding.awonarDialogOrderNumberPickerInputRate.setPrefix("")
                }
            }
        }
        launchAndRepeatWithViewLifecycle {
            orderViewModel.tradingData.collect { tradingData ->
                binding.awonarDialogOrderButtonTypeBuy.isEnabled = tradingData?.allowBuy == true
                binding.awonarDialogOrderButtonTypeSell.isEnabled = tradingData?.allowSell == true
            }
        }
        launchAndRepeatWithViewLifecycle {
            portfolioViewModel.portfolioState.collect {
                if (it != null) {
                    orderViewModel.setPortfolio(it)
                }
            }
        }
        /**
         * Amount Obsever
         */
        launchAndRepeatWithViewLifecycle {
            launch {
                orderActivityViewModel.exposureError.collect { error ->
                    binding.awoanrDialogOrderInputLayoutAmount.error = ""
                    error?.let {
                        binding.awoanrDialogOrderInputLayoutAmount.editText?.setText("${it.value}")
                        binding.awoanrDialogOrderInputLayoutAmount.error = (it.message ?: "")
                    }
                }
            }
            launch {
                orderViewModel.amountState.collect { amount ->
                    when (orderActivityViewModel.showAmount.value) {
                        false -> {
                            binding.awoanrDialogOrderInputLayoutAmount.setStartIconDrawable(0)
                            binding.awoanrDialogOrderInputLayoutAmount.editText?.setText(
                                "%.2f".format(amount.second)
                            )
                        }
                        true -> {
                            binding.awoanrDialogOrderInputLayoutAmount.setStartIconDrawable(R.drawable.awoanr_ic_dollar)
                            binding.awoanrDialogOrderInputLayoutAmount.editText?.setText(
                                "%.2f".format(amount.first)
                            )
                        }
                    }
                    validateExposure(amount = amount.first)
                }
            }
        }
        /**
         * Rate Obsever
         */
        launchAndRepeatWithViewLifecycle(Lifecycle.State.RESUMED) {
            launch {
                orderActivityViewModel.rateErrorState.collect {
                    binding.awoanrDialogOrderInputLayoutRate.error = ""
                    it?.let {
                        binding.awoanrDialogOrderInputLayoutRate.editText?.setText("${it.rate}")
                        binding.awoanrDialogOrderInputLayoutRate.error = (it.message ?: "")
                    }
                }
            }
            launch {
                orderViewModel.rateState.collect { rate ->
                    binding.awoanrDialogOrderInputLayoutRate.editText?.setText("${rate ?: ""}")
                    rate?.let {
                        orderActivityViewModel.validateRate(
                            it,
                            orderViewModel.priceState.value,
                            orderViewModel.instrument.value?.digit ?: 0
                        )
                    }
                }
            }
        }
        /**
         * stop loss Observer
         */
        launchAndRepeatWithViewLifecycle {
//            orderViewModel.takeProfit.collect { tp ->
//                binding.awonarDialogOrderViewNumberpickerCollapsibleTp.setNumber(tp)
//            }
        }

        launchAndRepeatWithViewLifecycle {
//            orderViewModel.stopLossState.collect { sl ->
//                binding.awonarDialogOrderViewNumberpickerCollapsibleSl.setNumber(sl)
//            }
        }
        launchAndRepeatWithViewLifecycle {
            launch {
                orderViewModel.leverageState.collect { leverage ->
                    val tradingData = orderViewModel.tradingData.value
                    if (binding.awonarDialogOrderRecyclerLeverages.adapter == null && tradingData != null) {
                        with(binding.awonarDialogOrderRecyclerLeverages) {
                            this.adapter = leverageAdapter
                            layoutManager =
                                GridLayoutManager(requireContext(), tradingData.leverages.size)
                        }
                    }
                    if (tradingData != null) {
                        val newList = tradingData.leverages.mapIndexed { index, s ->
                            LeverageSelectorAdapter.StepViewData(
                                label = "X$s",
                                value = s.toInt(),
                                start = index == 0,
                                end = index == tradingData.leverages.size - 1,
                                isCheck = s.toInt() == leverage
                            )
                        }.toMutableList()
                        Timber.e("$newList")
                        leverageAdapter.itemList = newList
                    }
//                    binding.awonarDialogOrderCollapseLeverage.setDescription("X$it")
//                    getOvernight()
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

    private fun observeActivityViewModel() {
        launchAndRepeatWithViewLifecycle {
            orderActivityViewModel.showAmount.collectIndexed { _, isShowAmount ->
                when (isShowAmount) {
                    true -> {
                        binding.awoanrDialogOrderInputLayoutAmount.setStartIconDrawable(R.drawable.awoanr_ic_dollar)
                        binding.awoanrDialogOrderButtonAmountToggle.text = "Unit"
                        binding.awoanrDialogOrderInputLayoutAmount.editText?.setText(
                            "%.2f".format(orderViewModel.amountState.value.first)
                        )
                    }
                    else -> {
                        binding.awoanrDialogOrderInputLayoutAmount.setStartIconDrawable(0)
                        binding.awoanrDialogOrderButtonAmountToggle.text = "Amount"
                        binding.awoanrDialogOrderInputLayoutAmount.editText?.setText(
                            "%.2f".format(orderViewModel.amountState.value.second)
                        )

                    }
                }
            }
        }
        launchAndRepeatWithViewLifecycle {
            orderActivityViewModel.isSetRate.collectIndexed { _, isAtMarket ->
                binding.awoanrDialogOrderInputLayoutRate.apply {
                    isEnabled = isAtMarket
                    hint = if (isAtMarket) "Open Rate" else "At Market"
                }
                when (isAtMarket) {
                    false -> orderViewModel.updateRate(null)
                    true -> orderViewModel.updateRate(orderViewModel.priceState.value)
                }
            }
        }
    }

    private fun observeError() {
        launchAndRepeatWithViewLifecycle {
            launch {
//                orderViewModel.amountError.collect {
//                    binding.awonarDialogOrderNumberPickerInputAmount.setHelp(it)
//                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val instrument = arguments?.getParcelable<Instrument?>(EXTRA_INSTRUMENT)
        val isBuy = arguments?.getBoolean(EXTRA_TYPE, true)
        orderViewModel.setInstrument(instrument)
        with(binding.awonarDialogOrderToggleOrderType) {
            orderViewModel.setIsBuy(isBuy)
            when (isBuy) {
                true -> check(R.id.awonar_dialog_order_button_type_buy)
                false -> check(R.id.awonar_dialog_order_button_type_sell)
                else -> {}
            }
        }
        binding.awonarDialogOrderToggleOrderType.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.awonar_dialog_order_button_type_sell -> orderViewModel.setIsBuy(false)
                    R.id.awonar_dialog_order_button_type_buy -> orderViewModel.setIsBuy(true)
                }
            }

        }
        binding.awonarDialogOrderImageIconClose.setOnClickListener {
            dismiss()
        }
//        binding.awonarDialogOrderButtonOpenTrade.setOnClickListener {
//            orderViewModel.submit()
//        }
        setupRate()
        setupAmount()
        setupLeverageAdapter()
        initStopLoss()
        initTakeProfit()
    }

    private fun setupAmount() {
        with(binding.awoanrDialogOrderButtonAmountToggle) {
            setOnClickListener {
                orderActivityViewModel.toggleShowAmount()
            }
        }
        binding.awoanrDialogOrderInputLayoutAmount.editText?.setOnFocusChangeListener { v, hasFocus ->
            val text = (v as TextInputEditText).text.toString()
            if (!hasFocus) {
                when (orderActivityViewModel.showAmount.value) {
                    true -> {
                        orderViewModel.updateAmount(
                            instrumentId = orderViewModel.instrument.value?.id ?: 0,
                            amount = text.toFloat(),
                            price = orderViewModel.priceState.value
                        )
                    }
                    false -> {
                        orderViewModel.updateUnits(
                            instrumentId = orderViewModel.instrument.value?.id ?: 0,
                            units = text.toFloat(),
                            price = orderViewModel.priceState.value
                        )
                    }
                }
            }
        }
//        binding.awonarDialogOrderToggleOrderAmountType.addOnButtonCheckedListener { _, checkedId, _ ->
//            when (checkedId) {
//                R.id.awonar_dialog_order_button_amount_amount -> {
//                    binding.awonarDialogOrderNumberPickerInputAmount.setNumber(orderViewModel.amountState.value.first)
//                    binding.awonarDialogOrderNumberPickerInputAmount.setPrefix("$")
//                }
//                R.id.awonar_dialog_order_button_amount_unit -> {
//                    binding.awonarDialogOrderNumberPickerInputAmount.setNumber(orderViewModel.amountState.value.second)
//                    binding.awonarDialogOrderNumberPickerInputAmount.setPrefix("")
//                }
//            }
//        }
    }

    private fun setupRate() {
        binding.awoanrDialogOrderInputLayoutRate.editText?.let { editText ->
            with(editText) {
                setOnFocusChangeListener { v, hasFocus ->
                    if (!hasFocus) {
                        val newRate = (v as EditText).text.toString()
                        if (newRate.isNotBlank()) {
                            orderViewModel.updateRate(newRate.toFloat())
                        }
                    }
                }
            }
        }

        binding.awoanrDialogOrderButtonRateToggle.setOnClickListener {
            val updateEnable = !binding.awoanrDialogOrderInputLayoutRate.isEnabled
            orderActivityViewModel.setAtMarket(updateEnable)
        }
    }

    private fun getOvernight() {
//        instrument?.let { instrument ->
//            orderActivityViewModel.getOvernightFeeDaliy(
//                instrumentId = instrument.id,
//                orderType = isBuy
//            )
//            orderActivityViewModel.getOvernightFeeWeek(
//                instrumentId = instrument.id,
//                orderType = isBuy
//            )
//        }

    }

    private fun initTakeProfit() {
//        binding.awonarDialogOrderViewNumberpickerCollapsibleTp.setDescriptionColor(R.color.awonar_color_primary)
//        binding.awonarDialogOrderViewNumberpickerCollapsibleTp.doAfterFocusChange =
//            { number, isLeft ->
//                if (isLeft) {
//                    orderViewModel.setAmountTp(number.first)
//                } else {
//                    orderViewModel.setRateTp(number.second)
//                }
//            }
    }

    private fun initStopLoss() {
//        binding.awonarDialogOrderViewNumberpickerCollapsibleSl.setDescriptionColor(R.color.awonar_color_orange)
//        binding.awonarDialogOrderViewNumberpickerCollapsibleSl.doAfterFocusChange =
//            { number, isLeft ->
//                if (isLeft) {
//                    orderViewModel.setAmountSl(number.first)
//                } else {
//                    orderViewModel.setRateSl(number.second)
//                }
//            }
    }


    private fun setupLeverageAdapter() {

//        binding.awonarDialogOrderToggleOrderType.addOnButtonCheckedListener { _, checkedId, _ ->
//            getOvernight()
//        }
//        binding.awonarDialogOrderCollapseLeverage.setTitle(getString(R.string.awonar_text_leverage))
//        binding.awonarDialogOrderCollapseLeverage.setAdapter(leverageAdapter)
    }

    private fun validateExposure(amount: Float) {
        orderViewModel.instrument.value?.let { instrument ->
            if (orderViewModel.leverageState.value > 0) {
                orderActivityViewModel.validateExposure(
                    id = instrument.id,
                    amount = amount,
                    leverage = orderViewModel.leverageState.value
                )
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

    override fun onDestroy() {
        viewModelStore.clear()
        super.onDestroy()
    }
}